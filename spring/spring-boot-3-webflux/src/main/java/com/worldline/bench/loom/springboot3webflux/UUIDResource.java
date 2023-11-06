package com.worldline.bench.loom.springboot3webflux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.worldline.bench.loom.springboot3webflux.client.ThirdPartyClient;
import com.worldline.bench.loom.springboot3webflux.entity.UUID;
import com.worldline.bench.loom.springboot3webflux.repository.UUIDRepository;

import reactor.core.publisher.Mono;

@RestController
public class UUIDResource {

    @Autowired
    UUIDRepository uuidRepository;

    @Autowired
    ThirdPartyClient client;

    @GetMapping(value = "/uuid/reactive", produces = "application/json")
    public Mono<UUID> uuid(
            @RequestParam(value = "rsCount", defaultValue = "1") int rsCount,
            @RequestParam(value = "rsSleep", defaultValue = "300") long rsSleep,
            @RequestParam(value = "dbCount", defaultValue = "1") int dbCount,
            @RequestParam(value = "dbSleep", defaultValue = "0.025") double dbSleep) {
        var mono = Mono.just(new UUID());
        for (int i = 0; i < Math.max(rsCount, dbCount); i++) {
            if (i < rsCount) {
                mono = mono.flatMap(item -> client.timed(rsSleep).map(r -> item));
            }
            if (i < dbCount) {
                mono = mono.flatMap(item -> uuidRepository.generateRandomUUID(dbSleep));
            }
        }
        return mono;
    }

}
