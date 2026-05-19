package org.acme.ai.dto;

import java.util.List;

public record GroqChatResponse(List<Choice> choices) {

    public record Choice(Message message) {}
    
    public record Message(String content, List<ToolCall> tool_calls) {}
    
    public record ToolCall(String id, String type, FunctionCall function) {}
    
    public record FunctionCall(String name, String arguments) {}
}