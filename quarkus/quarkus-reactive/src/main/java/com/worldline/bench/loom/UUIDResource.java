package com.worldline.bench.loom;

import java.time.Duration;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.worldline.bench.loom.client.ThirdPartySleepServiceClient;
import com.worldline.bench.loom.entity.UUID;
import com.worldline.bench.loom.repository.UUIDRepository;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
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
    @RestClient
    ThirdPartySleepServiceClient client;

    @Inject
    UUIDRepository repository;

    @GET
    @Path("/reactive")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<UUID> uuid(
        @QueryParam("wsCount") @DefaultValue("1") int wsCount,
        @QueryParam("wsSleep") @DefaultValue("300") long wsSleep,
        @QueryParam("dbCount") @DefaultValue("2") int dbCount,
        @QueryParam("dbSleep") @DefaultValue("0.025") double dbSleep
    ) {
        Log.debugv("UUID - reactive: wsCount={0}, wsSleep={1}, dbCount={2}, dbSleep={3} [{4}]",
                wsCount, wsSleep, dbCount, dbSleep, Thread.currentThread());
        /*Multi<UUID> ws = Multi.createFrom().range(0, wsCount)
            .onItem().transformToUniAndConcatenate(i -> client.timed(wsSleep))
            .onItem().transform(i -> new UUID());
        Multi<UUID> db = Multi.createFrom().range(0, dbCount)
            .onItem().transformToUniAndConcatenate(i -> repository.generateRandomUUID(dbSleep));*/

        var uni = Uni.createFrom().item(new UUID());
        for (int i = 0; i < Math.max(wsCount, dbCount); i++) {
            if (i < wsCount) {
                uni = uni.onItem().call(item -> client.timed(wsSleep));
            }
            if (i < dbCount) {
                uni = uni.onItem().transformToUni(item -> repository.generateRandomUUID(dbSleep));
            }
        }

        final var finalUni = uni;

        return Panache.withSession(() -> finalUni);
        // return Multi.createBy().concatenating().streams(ws, db).collect().last();
    }
}
