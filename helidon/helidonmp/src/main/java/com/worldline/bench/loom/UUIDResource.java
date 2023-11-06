package com.worldline.bench.loom;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.worldline.bench.loom.client.ThirdPartySleepServiceClient;
import com.worldline.bench.loom.entity.UUID;
import com.worldline.bench.loom.repository.UUIDRepository;

import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/uuid/virtual")
public class UUIDResource {

    @Inject
    UUIDRepository uuidRepository;

    @Inject
    @RestClient
    ThirdPartySleepServiceClient client;

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
                client.timed(wsSleep).toCompletableFuture().join();
                // Thread.sleep(300);
            }
            if (i < dbCount) {
                lastUuid = uuidRepository.generateRandom(dbSleep);
            }
        }
        return lastUuid;
    }
}
