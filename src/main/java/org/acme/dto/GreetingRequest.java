package org.acme.dto;

import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "GreetingRequest", description = "Requisição de saudação")
public record GreetingRequest(
    @Schema(description = "Nome da pessoa a saudar", examples = "João")
    @NotBlank(message = "Nome não pode estar vazio")
    String name
) {}
