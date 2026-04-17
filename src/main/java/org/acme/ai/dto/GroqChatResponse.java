package org.acme.ai.dto;

import java.util.List;

public record GroqChatResponse(
    List<Choice> choices
) {
    public String getContent() {
        if (choices == null || choices.isEmpty()) return "";
        return choices.get(0).message().content();
    }

    public record Choice(Message message) {}
    public record Message(String content) {}
}