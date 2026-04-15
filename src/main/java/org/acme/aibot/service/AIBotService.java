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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import jakarta.ws.rs.core.Response;


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
    public boolean validarTipoDocumento(UploadDocRequest documentoUpload) throws IllegalArgumentException {
        // logger.info("Iniciando criação de admin para: {}", request.email());

        List<String> TIPOS_PERMITIDOS = List.of(
        "application/pdf",
        "application/docx",
        "application/xlsx",
        "apllication/pptx",
        "application/html",
        "application/xhtml",
        "application/csv",
        "application/markdown",

        "image/png",
        "image/jpeg",
        "image/tiff",
        "image/bmp",
        "image/webp"
        );
        //arquivos suportados pelo docling

        final Tika TIKA = new Tika();

        try {
            File file = documentoUpload.document.uploadedFile().toFile();
            //documentoupload é o corpo inteiro da requsição, e document é o campo dentro dele
            String tipoDocumento = TIKA.detect(file); 

            if (!TIPOS_PERMITIDOS.contains(tipoDocumento)) {
                throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                        .entity("Tipo de arquivo inválido: " + tipoDocumento)
                        .build()
                );
            }
            return true;

        } catch (IOException e) {
            throw new WebApplicationException(
                Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao processar documento: " + e.getMessage())
                    .build()
            );
        }





    }

    @Override
    public UploadDocResponse uploadDocumentoLocalStack(UploadDocRequest documento) throws IllegalArgumentException {
        //TESTE PARA VE SE VALIDAR O TIPO DE DOCUMENTO FUNCIONA
        //FAZER UPLOAD PARA O LOCALSTACK AQUI DEPOIS
        
        if (validarTipoDocumento(documento) == true) {;
            return new UploadDocResponse(
                true,
                "Documento enviado com sucesso",
                "chave-gerada-no-s3"
            );
        } else {
            return new UploadDocResponse(
                false,
                "Falha ao enviar documento",
                null
            );
        }
    }

    @Override
    public UploadDocResponse uploadDetalhesDocumentoNoDB(UploadDocRequest documento) throws IllegalArgumentException {
        return;
    }
}