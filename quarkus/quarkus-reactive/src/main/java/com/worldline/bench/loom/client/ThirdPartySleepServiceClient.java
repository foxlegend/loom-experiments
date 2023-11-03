package com.worldline.bench.loom.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/sleep")
@RegisterRestClient(configKey = "sleep-service")
public interface ThirdPartySleepServiceClient {
    
    @GET
    @Path("/timed")
    public Uni<Void> timed(@QueryParam("time") Long time);
}
