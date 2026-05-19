package org.acme.ai;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/ai")
@Produces(MediaType.APPLICATION_JSON)
public class AiTestResource {

    private static final Logger logger = LoggerFactory.getLogger(AiTestResource.class);

    @Inject
    IConnectionAI aiConnection;

    @GET
    @Path("/test-tools")
    public String testTools(@QueryParam("q") String question) {
        if (question == null || question.isBlank()) {
            question = "Qual o calendário acadêmico de 2026 da Fatec Itaquera?";
        }

        logger.info("🧪 Teste Tool Calling - Pergunta: {}", question);

        try {
            // Como generateResponse retorna Uni<String>
            String response = aiConnection.generateResponse(question)
                    .await().indefinitely();

            return """
                {
                    "pergunta": "%s",
                    "resposta": "%s"
                }
                """.formatted(question, response.replace("\"", "\\\""));

        } catch (Exception e) {
            logger.error("Erro no teste de IA", e);
            return """
                {
                    "pergunta": "%s",
                    "erro": "Falha ao processar: %s"
                }
                """.formatted(question, e.getMessage());
        }
    }
}