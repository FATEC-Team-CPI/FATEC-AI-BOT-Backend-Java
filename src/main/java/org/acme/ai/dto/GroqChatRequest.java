package org.acme.ai.dto;

import java.util.List;
import java.util.Map;

public record GroqChatRequest(
    String model,
    List<Message> messages,
    int max_tokens,
    double temperature,
    List<Tool> tools,
    String tool_choice
) {

    public static GroqChatRequest of(String question, List<Tool> tools) {
        return new GroqChatRequest(
            "llama-3.3-70b-versatile",
            List.of(new Message("system", SYSTEM_PROMPT), new Message("user", question)),
            800,
            0.3,
            tools,
            "auto"
        );
    }

    public record Message(String role, String content) {}

    public record Tool(String type, Function function) {
        public static Tool of(Function function) {
            return new Tool("function", function);
        }
    }

    public record Function(String name, String description, Parameters parameters) {}

    public record Parameters(String type, Map<String, ParameterSchema> properties, List<String> required) {}

    public record ParameterSchema(String type, String description) {}

    private static final String SYSTEM_PROMPT = """
        Você é o FATEC AI Bot. Use as ferramentas disponíveis para responder com precisão.
        Sempre que necessário, chame list_available_documents primeiro.
        """;
}