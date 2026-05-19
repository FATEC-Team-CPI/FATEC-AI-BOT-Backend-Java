package org.acme.ai;

import io.smallrye.mutiny.Uni;

public interface IConnectionAI {

    /**
     * Gera resposta usando Groq com suporte a Tool Calling (MCP)
     */
    Uni<String> generateResponse(String question);
}