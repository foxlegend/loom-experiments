package com.worldline.bench.loom;

import java.time.Duration;
import java.time.LocalDateTime;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/sleep")
public class SleepResource {
    
    @GET
    @Path("/reactive")
    public Uni<Long> sleep(@QueryParam("duration") @DefaultValue("300") long duration) {
        return Uni.createFrom().item(LocalDateTime.now())
                .onItem().delayIt().by(Duration.ofMillis(duration))
                .onItem().transform(item -> Duration.between(item, LocalDateTime.now()).toMillis());
    }
}
