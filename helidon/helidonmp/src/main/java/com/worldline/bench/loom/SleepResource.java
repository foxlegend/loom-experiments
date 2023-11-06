package com.worldline.bench.loom;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/sleep")
public class SleepResource {

    private static final Logger LOGGER = Logger.getLogger(SleepResource.class.getName());

    @GET
    @Path("/virtual")
    public Long virtualThreads(@QueryParam("duration") @DefaultValue("300") long duration) throws InterruptedException {
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(String.format("SLEEP - virtualThreads: [%1]", Thread.currentThread()));
        }
        return this.sleep(duration);
    }

    private Long sleep(long duration) throws InterruptedException {
        var start = LocalDateTime.now();
        Thread.sleep(duration);
        return Duration.between(start, LocalDateTime.now()).toMillis();
    }
}
