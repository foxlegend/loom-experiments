package com.worldline.bench.loom.springboot3webflux;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.worldline.bench.loom.springboot3webflux.client.ThirdPartySleepServiceClient;
import com.worldline.bench.loom.springboot3webflux.entity.UUID;
import com.worldline.bench.loom.springboot3webflux.repository.UUIDRepository;

import reactor.core.publisher.Mono;

@RestController
public class UUIDResource {

    @Autowired
    UUIDRepository uuidRepository;

    @Autowired
    ThirdPartySleepServiceClient client;

    @GetMapping(value = "/uuid", produces = "application/json")
    public Mono<UUID> uuid(
            @RequestParam(value = "rsCount", defaultValue = "1") int rsCount,
            @RequestParam(value = "rsSleep", defaultValue = "300") long rsSleep,
            @RequestParam(value = "dbCount", defaultValue = "1") int dbCount,
            @RequestParam(value = "dbSleep", defaultValue = "0.025") double dbSleep) {
        return Mono.just("1").flatMap(item -> {
            return client.timed(rsSleep);
        })
        .flatMap(item -> {
            return uuidRepository.generateRandomUUID(dbSleep);
        })
        .flatMap(item -> {
            return uuidRepository.generateRandomUUID(dbSleep);
        });
    }

}
