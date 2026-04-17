package org.acme.chat.dto;

import java.time.Instant;

public record ChatMessageResponse(
    String sessionId,
    String answer,
    String type,         
    Instant timestamp
) {}