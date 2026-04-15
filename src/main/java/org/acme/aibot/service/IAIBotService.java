package org.acme.aibot.service;

import org.acme.aibot.dto.UploadDocRequest;
import org.acme.aibot.dto.UploadDocResponse;


/**
 * Service Interface: Contrato da camada de negócio
 */
public interface IAIBotService {
    
    /**
     * Caso de uso: Validar se documento tem tipo valido
     * @param request documento
     * @return status se valido ou inavlido
     * @throws IllegalArgumentException 
     */
    boolean validarTipoDocumento(UploadDocRequest documento) throws IllegalArgumentException;

    /**
     * Caso de uso: Fazer upload de documento no localstack
     * @param request documento
     * @return response se documento fez upload com sucesso ou não
     * @throws IllegalArgumentException 
     */
    UploadDocResponse uploadDocumentoLocalStack(UploadDocRequest documento) throws IllegalArgumentException;

    /**
     * Caso de uso: Fazer upload de documento no DynamondDB
     * @param request documento
     * @return response se os detalhes documento fez upload com sucesso ou não
     * @throws IllegalArgumentException 
     */
    UploadDocResponse uploadDetalhesDocumentoNoDB(UploadDocRequest metadados) throws IllegalArgumentException;
    
    

}

