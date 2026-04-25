package org.acme.ai;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.ai.dto.GroqChatRequest;
import org.acme.ai.dto.GroqChatResponse;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class GroqConnectionAI implements IConnectionAI {

    private static final Logger logger = LoggerFactory.getLogger(GroqConnectionAI.class);

    @RestClient
    GroqRestClient groqClient;

    @ConfigProperty(name = "groq.api.key")
    String apiKey;

    @Override
    public String generateResponse(String question) {
        logger.info("Enviando pergunta para Groq: {}", question);

        try {
            // Verifica se a chave foi carregada corretamente do arquivo de propriedades
            if (apiKey == null || apiKey.isBlank()) {
                logger.error("ERRO: groq.api.key não encontrada no application.properties");
                return "Erro: Chave da IA não configurada no servidor.";
            }

            String auth = "Bearer " + apiKey;
            GroqChatRequest request = GroqChatRequest.of(question);
            GroqChatResponse response = groqClient.chatCompletion(auth, request);

            if (response == null || response.getContent() == null) {
                return "Não consegui gerar uma resposta da IA.";
            }

            return response.getContent().trim();

        } catch (Exception e) {
            logger.error("Erro ao conectar com o serviço de IA", e);
            return "Desculpe, ocorreu um erro técnico ao conectar com a IA.";
        }
    }
}