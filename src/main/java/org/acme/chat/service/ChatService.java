package org.acme.chat.service;

import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.ai.IConnectionAI;
import org.acme.chat.dto.ChatMessageRequest;
import org.acme.chat.dto.ChatMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class ChatService implements IChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    private static final List<String> INJECTION_KEYWORDS = List.of(
        "ignore", "esqueça", "esqueca", "agora você é", "agora vc é",
        "novo papel", "finja", "simule", "a partir de agora",
        "suas instruções anteriores", "suas ordens anteriores"
    );

    @Inject
    IConnectionAI aiConnection;

    private boolean contemInjection(String question) {
        String lower = question.toLowerCase();
        return INJECTION_KEYWORDS.stream().anyMatch(lower::contains);
    }

    @Override
    public Uni<ChatMessageResponse> processMessage(ChatMessageRequest request) {
        logger.info("Processando mensagem do chat - sessionId: {}", request.sessionId());

        if (contemInjection(request.question())) {
            logger.warn("Tentativa de prompt injection detectada - sessionId: {}", request.sessionId());
            return Uni.createFrom().item(new ChatMessageResponse(
                request.sessionId(),
                "Olá! Sou o FATEC AI Bot e só posso responder dúvidas relacionadas à FATEC. Posso te ajudar com algo? 😊",
                "BOT_REPLY",
                Instant.now()
            ));
        }

        // ✅ Correção: Como generateResponse agora retorna Uni<String>
        return aiConnection.generateResponse(request.question())
                .map(respostaIA -> new ChatMessageResponse(
                    request.sessionId(),
                    respostaIA,
                    "BOT_REPLY",
                    Instant.now()
                ))
                .onFailure().recoverWithItem(e -> {
                    logger.error("Erro no processamento da IA", e);
                    return new ChatMessageResponse(
                        request.sessionId(),
                        "Desculpe, ocorreu um erro técnico ao processar sua solicitação.",
                        "BOT_REPLY",
                        Instant.now()
                    );
                });
    }
}