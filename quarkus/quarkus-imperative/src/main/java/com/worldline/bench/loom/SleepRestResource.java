package com.worldline.bench.loom;

import java.time.Duration;
import java.time.LocalDateTime;

import io.quarkus.logging.Log;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/sleep")
public class SleepRestResource {
    
    @GET
    @Path("/platform")
    public Long platformThreads(@QueryParam("duration") @DefaultValue("300") long duration) {
        Log.debugv("SLEEP - platformThreads: [{0}]", Thread.currentThread());
        return this.sleep(duration);
    }

    @GET
    @Path("/virtual")
    @RunOnVirtualThread
    public Long virtualThreads(@QueryParam("duration") @DefaultValue("300") long duration) {
        Log.debugv("SLEEP - virtualThreads: [{0}]", Thread.currentThread());
        return this.sleep(duration);
    }

    private Long sleep(long duration) {
        var start = LocalDateTime.now();
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Duration.between(start, LocalDateTime.now()).toMillis();
    }
}
