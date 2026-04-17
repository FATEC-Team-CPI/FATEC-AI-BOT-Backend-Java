package org.acme.ai.dto;

import java.util.List;

public record GroqChatRequest(
    String model,
    List<Message> messages,
    int max_tokens,
    double temperature
) {
    public static GroqChatRequest of(String question) {
        return new GroqChatRequest(
            "llama-3.3-70b-versatile",   // modelo rápido e bom (pode mudar)
            List.of(
                new Message("system", "Você é o FATEC AI Bot, um assistente útil especializado em informações da Fatec (calendário, matrícula, editais, TCC, etc.). Responda de forma clara, educada e direta em português."),
                new Message("user", question)
            ),
            500,      // limite de tokens da resposta
            0.7
        );
    }

    public record Message(String role, String content) {}
}