package com.worldline.bench.loom;

import java.time.Duration;
import java.time.LocalDateTime;

import io.quarkus.logging.Log;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/cpu")
public class CPURestResource {

    @GET
    @Path("/platform")
    public Long platformThreads(@QueryParam("duration") @DefaultValue("300") long duration) {
        Log.debugv("CPU - platformThreads: [{0}]", Thread.currentThread());
        return this.cpuComputations(duration);
    }

    @GET
    @Path("/virtual")
    @RunOnVirtualThread
    public Long virtualThreads(@QueryParam("duration") @DefaultValue("300") long duration) {
        Log.debugv("CPU - virtualThreads: [{0}]", Thread.currentThread());
        return this.cpuComputations(duration);
    }

    private Long cpuComputations(long duration) {
        Long loopCount = 0L;
        LocalDateTime start = LocalDateTime.now();
        while (Duration.between(start, LocalDateTime.now()).toMillis() < duration) {
            loopCount++;
        }
        return loopCount;
    }
}
