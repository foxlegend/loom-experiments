package com.worldline.bench.loom;

import java.time.Duration;
import java.time.LocalDateTime;

import org.jboss.resteasy.reactive.RestResponse;

import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/cpu")
public class CPUResource {

    @GET
    @Path("/reactive")
    @Blocking
    public Uni<Long> cpu(@QueryParam("duration") @DefaultValue("300") long duration) {
        return Uni.createFrom().item(0L)
                .onItem().transform(item -> {
                    LocalDateTime start = LocalDateTime.now();
                    while (Duration.between(start, LocalDateTime.now()).toMillis() < duration) {
                        item++;
                    }
                    return item;
                });
    }
}
