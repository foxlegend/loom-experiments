package com.worldline.bench.loom.springboot3webflux.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.worldline.bench.loom.springboot3webflux.entity.UUID;

import reactor.core.publisher.Mono;

public interface UUIDRepository extends R2dbcRepository<UUID, Long>  {

    @Query("SELECT floor(random()*10000+1) as id, gen_random_uuid() as uuid, pg_sleep(:sleep)")
    public Mono<UUID> generateRandomUUID(double sleep);
}
