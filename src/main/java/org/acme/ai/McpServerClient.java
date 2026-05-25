package org.acme.ai;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Cliente HTTP para comunicar com o MCP Server Python
 * 
 * Faz chamadas HTTP para:
 * - GET /tools/list_available_documents
 * - GET /tools/search_fatec_documents?query=X&document_type=Y
 */
@ApplicationScoped
public class McpServerClient {

    private static final Logger logger = LoggerFactory.getLogger(McpServerClient.class);

    @ConfigProperty(name = "mcp.server.url", defaultValue = "http://mcp-server:8001")
    String mcpServerUrl;

    private Client httpClient;

    /**
     * Initializa o cliente HTTP lazy
     */
    private Client getHttpClient() {
        if (httpClient == null) {
            httpClient = ClientBuilder.newClient();
        }
        return httpClient;
    }

    /**
     * Lista todos os documentos disponíveis no MCP Server
     */
    public String listAvailableDocuments() {
        try {
            logger.info("══════════════════════════════════════════════════════════════════════");
            logger.info("🌐 Iniciando request HTTP para MCP Server");
            logger.info("   Endpoint: GET {}/tools/list_available_documents", mcpServerUrl);
            logger.info("   Timestamp: {}", java.time.LocalDateTime.now());
            
            String response = getHttpClient()
                .target(mcpServerUrl)
                .path("/tools/list_available_documents")
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

            logger.info("✅ Response recebido com sucesso ({} caracteres)", response.length());
            logger.info("══════════════════════════════════════════════════════════════════════");
            return response;

        } catch (Exception e) {
            logger.error("❌ Erro ao listar documentos do MCP: {}", e.getMessage(), e);
            logger.error("══════════════════════════════════════════════════════════════════════");
            // Fallback para dados locais se MCP falhar
            return getFallbackDocuments();
        }
    }

    /**
     * Busca informações em um documento específico
     */
    public String searchFatecDocuments(String query, String documentType) {
        try {
            logger.info("══════════════════════════════════════════════════════════════════════");
            logger.info("🌐 Iniciando request HTTP para MCP Server");
            logger.info("   Endpoint: GET {}/tools/search_fatec_documents", mcpServerUrl);
            logger.info("   Parâmetros:");
            logger.info("     - query: '{}'", query);
            logger.info("     - document_type: '{}'", documentType);
            logger.info("   Timestamp: {}", java.time.LocalDateTime.now());
            
            String response = getHttpClient()
                .target(mcpServerUrl)
                .path("/tools/search_fatec_documents")
                .queryParam("query", query)
                .queryParam("document_type", documentType)
                .request(MediaType.APPLICATION_JSON)
                .get(String.class);

            logger.info("✅ Response recebido com sucesso ({} caracteres)", response.length());
            logger.info("══════════════════════════════════════════════════════════════════════");
            return response;

        } catch (Exception e) {
            logger.error("❌ Erro ao buscar documentos do MCP: {}", e.getMessage(), e);
            logger.error("══════════════════════════════════════════════════════════════════════");
            // Fallback para dados locais se MCP falhar
            return getFallbackSearchResults(documentType);
        }
    }

    /**
     * Fallback caso MCP Server esteja indisponível
     */
    private String getFallbackDocuments() {
        logger.warn("⚠️ Usando fallback local para listagem de documentos");
        return """
            Documentos disponíveis:
            1. calendario_academico - Datas de aulas, provas, recessos e férias
            2. edital_vestibular - Processo seletivo, inscrições, vagas
            3. grade_curricular - Disciplinas por curso e semestre
            4. regulamento - Normas, regras e regulamentos internos
            5. contato - Endereço, telefones, e-mails, horários
            """;
    }

    /**
     * Fallback para resultados de busca
     */
    private String getFallbackSearchResults(String documentType) {
        logger.warn("⚠️ Usando fallback local para busca de: {}", documentType);
        
        return switch(documentType) {
            case "calendario_academico" -> """
                Recesso de julho: 14/07 a 18/07/2025
                Férias de dezembro: 15/12/2025 a 01/02/2026
                Provas do semestre 1: 10/06 a 20/06/2025
                Aulas iniciam em: 3 de março de 2025
                """;
            case "edital_vestibular" -> """
                Número de vagas: 100 por curso
                Inscrições: 01/01 a 15/01/2026
                Prova: 20/02/2026
                Resultado: 10/03/2026
                """;
            case "grade_curricular" -> """
                Curso: Análise e Desenvolvimento de Sistemas
                Semestre 1: Programação I, Lógica, Banco de Dados Básico
                Semestre 2: Programação II, Estrutura de Dados, SQL
                Semestre 3: POO, Arquitetura de Software, Web
                """;
            case "regulamento" -> """
                Frequência mínima: 75%
                Nota mínima para aprovação: 6.0
                Recuperação: Prova final para média < 7.0
                Prazo de desistência: 25% do semestre
                """;
            case "contato" -> """
                Endereço: Avenida Itaquera, 1234 - São Paulo - SP
                Telefone: (11) 5555-1234
                E-mail: contato@fatec.sp.gov.br
                Horário: 8h - 22h
                """;
            default -> "Documento '" + documentType + "' não encontrado";
        };
    }
}
