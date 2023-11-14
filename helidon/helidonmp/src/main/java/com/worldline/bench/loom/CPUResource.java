package com.worldline.bench.loom;

import java.time.Duration;
import java.time.LocalDateTime;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/cpu")
public class CPUResource {
    
    @GET
    @Path("/virtual")
    public Long cpu(@QueryParam("duration") @DefaultValue("300") long duration) {
        Long loopCount = 0L;
        LocalDateTime start = LocalDateTime.now();
        while(Duration.between(start, LocalDateTime.now()).toMillis() < duration) {
            loopCount++;
        }
        return loopCount;
    }
}
