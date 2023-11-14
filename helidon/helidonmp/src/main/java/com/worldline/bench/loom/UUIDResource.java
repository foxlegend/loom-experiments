package com.worldline.bench.loom;

import java.net.URI;
import java.util.concurrent.Semaphore;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.worldline.bench.loom.client.ThirdPartySleepServiceClient;
import com.worldline.bench.loom.entity.UUID;
import com.worldline.bench.loom.repository.UUIDRepository;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.MediaType;

@Path("/uuid/virtual")
@ApplicationScoped
public class UUIDResource {

    @Inject
    @ConfigProperty(name = "sleep-service.maxConnections", defaultValue = "-1")
    private int maxConnections;

    @Inject
    @ConfigProperty(name="sleep-service/mp-rest/url", defaultValue = "http://localhost:8090")
    private String sleepServiceUrl;

    private Semaphore semaphore;

    @Inject
    UUIDRepository uuidRepository;

    @Inject
    @RestClient
    ThirdPartySleepServiceClient client;

    @Inject
    Client otherCLient;

    @PostConstruct
    public void init() {
        if (maxConnections > 0) {
            semaphore = new Semaphore(maxConnections);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public UUID uuid(
            @QueryParam("rsCount") @DefaultValue("1") int wsCount,
            @QueryParam("rsSleep") @DefaultValue("300") long wsSleep,
            @QueryParam("dbCount") @DefaultValue("2") int dbCount,
            @QueryParam("dbSleep") @DefaultValue("0.025") double dbSleep) throws InterruptedException {
        return simulated(wsCount, wsSleep, dbCount, dbSleep);
    }

    private UUID simulated(int wsCount, long wsSleep, int dbCount, double dbSleep) throws InterruptedException {
        UUID lastUuid = new UUID();
        for (int i = 0; i < Math.max(wsCount, dbCount); i++) {
            if (i < wsCount) {
                if (maxConnections > 0) {
                    semaphore.acquire();
                }
                try {
                    // client.timed(wsSleep).toCompletableFuture().join();
                    var uri = URI.create(sleepServiceUrl + "/sleep/timed?time=" + wsSleep);
                    otherCLient.target(uri).request().get();
                } finally {
                    if (maxConnections > 0) {
                        semaphore.release();
                    }
                }
            }
            if (i < dbCount) {
                lastUuid = uuidRepository.generateRandom(dbSleep);
            }
        }
        return lastUuid;
    }
}
