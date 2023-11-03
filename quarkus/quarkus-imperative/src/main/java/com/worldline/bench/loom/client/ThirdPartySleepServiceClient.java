package com.worldline.bench.loom.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/sleep")
@RegisterRestClient(configKey = "sleep-service")
public interface ThirdPartySleepServiceClient {
    
    @GET
    @Path("/timed")
    void timed(@QueryParam("time") Long time);
}
