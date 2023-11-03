package com.worldline.bench.loom;

import java.time.Duration;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

import io.smallrye.mutiny.Uni;

@Path("/sleep")
public class SleepResource {
    
    @GET
    @Path("/timed")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Long> timed(@QueryParam("time") @DefaultValue("250") long time) {
        return Uni.createFrom().item(time).onItem().delayIt().by(Duration.ofMillis(time));
    }
}
