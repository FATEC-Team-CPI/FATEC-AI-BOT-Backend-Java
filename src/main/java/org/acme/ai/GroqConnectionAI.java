package org.acme.ai;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.ai.dto.GroqChatRequest;
import org.acme.ai.dto.GroqChatResponse;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class GroqConnectionAI implements IConnectionAI {

    private static final Logger logger = LoggerFactory.getLogger(GroqConnectionAI.class);

    @RestClient
    GroqRestClient groqClient;

    @ConfigProperty(name = "groq.api.key", defaultValue = "dummy-key")
    String apiKey;

    // Definição das ferramentas (MCP)
    private final List<GroqChatRequest.Tool> tools = List.of(
        GroqChatRequest.Tool.of(new GroqChatRequest.Function(
            "list_available_documents",
            "Retorna a lista completa de documentos disponíveis na base de conhecimento da FATEC.",
            new GroqChatRequest.Parameters("object", Map.of(), List.of())
        )),
        GroqChatRequest.Tool.of(new GroqChatRequest.Function(
            "search_fatec_documents",
            "Busca trechos relevantes dentro de um documento específico.",
            new GroqChatRequest.Parameters("object",
                Map.of(
                    "query", new GroqChatRequest.ParameterSchema("string", "Pergunta ou termo de busca do usuário"),
                    "document_type", new GroqChatRequest.ParameterSchema("string", "Tipo exato do documento (ex: calendario_academico, edital_vestibular)")
                ),
                List.of("query", "document_type")
            )
        ))
    );

    @Override
    public Uni<String> generateResponse(String question) {
        return Uni.createFrom().item(() -> callGroqWithTools(question))
                .runSubscriptionOn(io.smallrye.mutiny.infrastructure.Infrastructure.getDefaultWorkerPool());
    }

    private String callGroqWithTools(String question) {
        try {
            if (apiKey == null || apiKey.isBlank() || "dummy-key".equals(apiKey)) {
                return "Chave Groq não configurada.";
            }

            String auth = "Bearer " + apiKey;
            GroqChatRequest request = GroqChatRequest.of(question, tools);

            GroqChatResponse response = groqClient.chatCompletion(auth, request);

            if (hasToolCalls(response)) {
                logger.info("🛠️ Groq chamou ferramenta(s) MCP");
                return handleToolCalls(response, question, auth);
            }

            return extractContent(response);

        } catch (Exception e) {
            logger.error("Erro ao chamar Groq", e);
            return "Desculpe, ocorreu um erro ao processar sua pergunta.";
        }
    }

    private boolean hasToolCalls(GroqChatResponse response) {
        if (response == null || response.choices().isEmpty()) return false;
        var msg = response.choices().get(0).message();
        return msg.tool_calls() != null && !msg.tool_calls().isEmpty();
    }

    private String handleToolCalls(GroqChatResponse response, String question, String auth) {
        // Simulação MCP 
        logger.info("🔧 Simulando resposta do MCP para ferramentas...");

        // LUH ou quem foi, colocar o MCP real aqui depois, usando as tool_calls do response para decidir qual função chamar e com quais argumentos
        return """
            Entendi sua pergunta: "%s"
            
            Consultei os documentos disponíveis da FATEC e encontrei informações relevantes.
            Estou preparando a resposta com base nos conteúdos oficiais...
            
            [Simulação de fluxo MCP/Tool Calling - em breve será real]
            """.formatted(question);
    }

    private String extractContent(GroqChatResponse response) {
        if (response == null || response.choices().isEmpty()) return "Sem resposta.";
        var msg = response.choices().get(0).message();
        return msg.content() != null ? msg.content().trim() : "Sem conteúdo.";
    }
}