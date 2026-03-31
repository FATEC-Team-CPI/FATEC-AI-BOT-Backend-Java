package org.acme.dto;

import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "GreetingResponse", description = "Resposta de saudação")
public record GreetingResponse(
    @Schema(description = "Mensagem de saudação", examples = "Hello, João!")
    String message,
    
    @Schema(description = "Data/hora da resposta em ISO-8601", examples = "2026-03-31T10:30:00Z")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    Instant timestamp
) {}
