package org.acme.aibot.service;

import org.acme.users.repository.IUserRepository;
import org.acme.aibot.dto.UploadDocRequest;
import org.acme.aibot.dto.UploadDocResponse;
import org.acme.aibot.service.IAIBotService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;

import org.slf4j.LoggerFactory;
import org.apache.tika.Tika;
import java.nio.file.Path;
import java.util.List;


/**
 * Service Implementation: Lógica de negócio concreta
 * Orquestra as operações do domínio com o repositório
 * Responsável por validações de negócio e transformações
 * 
 * Implementação de: IIABotService
 */
@ApplicationScoped
public class AIBotService implements IAIBotService {
    // private static final Logger logger = LoggerFactory.getLogger(AIBotService.class);
    
    // @Inject
    // IUserRepository repository;



    @Override
    public Boolean validarDocumento(UploadDocRequest documento) throws Exception {
        // logger.info("Iniciando criação de admin para: {}", request.email());

        List<String> TIPOS_PERMITIDOS = List.of(
        "application/pdf",
        "image/png",
        "image/jpeg"
        );

        final Tika TIKA = new Tika();

        String tipoDocumento = TIKA.detect(documento); 
        //tipo documento

        if (!TIPOS_PERMITIDOS.contains(tipoDocumento)) {
            throw new WebApplicationException(
                Response.status(Response.Status.BAD_REQUEST)
                    .entity("Tipo de arquivo inválido: " + tipoDocumento)
                    .build()
            );
            return false;
        }

        return true;

    }

    @Override
    public UploadDocResponse uploadDocumentoLocalStack(UploadDocRequest documento) throws Exception {
        return;
    }

    @Override
    public UploadDocResponse uploadDetalhesDocumentoNoDB(UploadDocRequest documento) throws Exception {
        return;
    }
}