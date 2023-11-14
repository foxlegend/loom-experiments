package com.worldline.bench.loom.client;

import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/sleep")
@RegisterRestClient(configKey = "sleep-service")
@ApplicationScoped
public interface ThirdPartySleepServiceClient {
    
    @GET
    @Path("/timed")
    CompletionStage<Void> timed(@QueryParam("time") Long time);
}
