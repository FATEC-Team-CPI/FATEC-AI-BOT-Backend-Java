package org.acme.chat.service;

import io.smallrye.mutiny.Uni;
import org.acme.chat.dto.ChatMessageRequest;
import org.acme.chat.dto.ChatMessageResponse;

public interface IChatService {
    Uni<ChatMessageResponse> processMessage(ChatMessageRequest request);
}