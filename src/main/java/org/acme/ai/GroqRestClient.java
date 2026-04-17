package org.acme.ai;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.acme.ai.dto.GroqChatRequest;
import org.acme.ai.dto.GroqChatResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/chat/completions")
@RegisterRestClient(configKey = "groq-api")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface GroqRestClient {

    @POST
    GroqChatResponse chatCompletion(
        @HeaderParam("Authorization") String authorization,
        GroqChatRequest request
    );
}