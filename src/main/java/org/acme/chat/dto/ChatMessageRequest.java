package org.acme.chat.dto;

import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ChatMessageRequest(
    @NotBlank(message = "sessionId é obrigatório")
    @JsonProperty("sessionId")
    String sessionId,

    @NotBlank(message = "Pergunta não pode estar vazia")
    @JsonProperty("question")
    String question
) {}