package com.worldline.bench.loom;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.worldline.bench.loom.client.ThirdPartySleepServiceClient;
import com.worldline.bench.loom.entity.UUID;
import com.worldline.bench.loom.repository.UUIDRepository;

import io.quarkus.logging.Log;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/uuid")
public class UUIDResource {

    @Inject
    UUIDRepository uuid;

    @Inject
    @RestClient
    ThirdPartySleepServiceClient client;

    @GET
    @Path("/platform")
    @Produces(MediaType.APPLICATION_JSON)
    public UUID uuidPlatform(
            @QueryParam("rsCount") @DefaultValue("1") int wsCount,
            @QueryParam("rsSleep") @DefaultValue("300") long wsSleep,
            @QueryParam("dbCount") @DefaultValue("2") int dbCount,
            @QueryParam("dbSleep") @DefaultValue("0.025") double dbSleep) {
        Log.debugv("UUID - platformThreads: wsCount={0}, wsSleep={1}, dbCount={2}, dbSleep={3} [{4}]",
                wsCount, wsSleep, dbCount, dbSleep, Thread.currentThread());
        return simulated(wsCount, wsSleep, dbCount, dbSleep);
    }

    @GET
    @Path("/virtual")
    @Produces(MediaType.APPLICATION_JSON)
    @RunOnVirtualThread
    public UUID uuidVirtual(
            @QueryParam("rsCount") @DefaultValue("1") int wsCount,
            @QueryParam("rsSleep") @DefaultValue("300") long wsSleep,
            @QueryParam("dbCount") @DefaultValue("2") int dbCount,
            @QueryParam("dbSleep") @DefaultValue("0.025") double dbSleep) {
        Log.debugv("UUID - virtualThreads: wsCount={0}, wsSleep={1}, dbCount={2}, dbSleep={3} [{4}]",
                wsCount, wsSleep, dbCount, dbSleep, Thread.currentThread());

        return simulated(wsCount, wsSleep, dbCount, dbSleep);
    }

    private UUID simulated(int wsCount, long wsSleep, int dbCount, double dbSleep) {
        UUID lastUuid = null;
        for (int i = 0; i < Math.max(wsCount, dbCount); i++) {
            if (i < wsCount) {
                client.timed(wsSleep);
            }
            if (i < dbCount) {
                lastUuid = uuid.generateRandom(dbSleep);
            }
        }
        return lastUuid;
    }

}
