package org.acme.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.acme.chat.dto.ChatMessageRequest;
import org.acme.chat.service.IChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerEndpoint("/ws/chat")
@ApplicationScoped
public class ChatWebSocket {

    private static final Logger logger = LoggerFactory.getLogger(ChatWebSocket.class);

    @Inject
    IChatService chatService;

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @OnOpen
    public void onOpen(Session session) {
        logger.info("Nova conexão WebSocket aberta - ID: {}", session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            // Deserializa a requisição
            ChatMessageRequest request = mapper.readValue(message, ChatMessageRequest.class);
            logger.info("Processando mensagem via Uni: {}", request.question());

          
            chatService.processMessage(request)
                .subscribe().with(
                    response -> {
                        try {
                            String jsonResponse = mapper.writeValueAsString(response);
                            session.getAsyncRemote().sendText(jsonResponse, result -> {
                                if (!result.isOK()) {
                                    logger.error("Erro ao enviar resposta ao cliente", result.getException());
                                }
                            });
                        } catch (Exception e) {
                            logger.error("Erro ao serializar resposta para JSON", e);
                        }
                    },
                    failure -> {
                        logger.error("Erro no processamento reativo da IA", failure);
                        session.getAsyncRemote().sendText("{\"error\":\"Falha ao processar IA\"}");
                    }
                );

        } catch (Exception e) {
            logger.error("Erro ao processar mensagem WebSocket", e);
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        logger.info("Conexão fechada - ID: {}", session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.error("Erro na conexão WebSocket - ID: {}", session.getId(), throwable);
    }
}