package org.acme.ai;

import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tools que chamam o MCP Server Python
 * 
 * Classe separada (não interna) para que CDI possa injeta McpServerClient corretamente
 */
@ApplicationScoped
public class FatecTools {
    private static final Logger logger = LoggerFactory.getLogger(FatecTools.class);

    @Inject
    McpServerClient mcpClient;
    
    // Chamado quando a instância é criada
    public void init() {
        logger.info("🔨 FatecTools inicializado");
        if (mcpClient == null) {
            logger.error("❌ ERRO CRÍTICO: McpServerClient é NULL em FatecTools!");
        } else {
            logger.info("✅ McpServerClient injetado com sucesso em FatecTools");
        }
    }

    @Tool("Lista todos os documentos disponíveis na FATEC")
    public String list_available_documents() {
        logger.info("🔍 Tool chamada: list_available_documents()");
        logger.info("📡 Delegando para MCP Server...");
        return mcpClient.listAvailableDocuments();
    }

    @Tool("Busca informações em um documento específico da FATEC")
    public String search_fatec_documents(String query, String document_type) {
        logger.info("🔍 Tool chamada: search_fatec_documents(query='{}', type='{}')", query, document_type);
        logger.info("📡 Delegando para MCP Server...");
        return mcpClient.searchFatecDocuments(query, document_type);
    }
}
