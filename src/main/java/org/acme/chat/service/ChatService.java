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

@ApplicationScoped
public class ChatService implements IChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    @Inject
    IConnectionAI aiConnection;

    @Override
    public Uni<ChatMessageResponse> processMessage(ChatMessageRequest request) {
        logger.info("Processando mensagem do chat - sessionId: {}", request.sessionId());

        return Uni.createFrom().item(() -> aiConnection.generateResponse(request.question()))
                .runSubscriptionOn(Infrastructure.getDefaultWorkerPool())
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