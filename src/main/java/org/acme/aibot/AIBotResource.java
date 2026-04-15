import org.acme.aibot.service.IAIBotService;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;

@Path("/doc")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Doc", description = "Gerenciamento de documentos para o IA Bot")
public class AIBotResource {
    private static final Logger logger = LoggerFactory.getLogger(AIBotResource.class);
    
    @Inject
    IAIBotService service;

      
    /**
     * POST /admin
     * Fazer upload arquivo para localstack
     */
    @POST
    @Path("/upload")
    @Operation(summary = "Upload arquivo", description = "Fazer upload de um arquivo para o localstack")
    @APIResponse(responseCode = "201", description = "Admin criado com sucesso",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserResponse.class)))
    @APIResponse(responseCode = "400", description = "Dados inválidos")

    public Response upload(UploadDocRequest request) {
        //ENVIAR DOCUMENTO PARA VALIDAR, SE VALIDO ENVIAR PRO LOCALSTACK, SE INVALIDO RETORNAR ERRO
        //EXTRAIR METADADOS DO DOCUMENTO AQUI E ENVIAR PRO DB
    }

}