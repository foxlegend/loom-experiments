package com.worldline.bench.loom.springboot3webflux;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
public class CPUResource {
    
    @GetMapping(value="/cpu/reactive")
    public Mono<Long> cpu(@RequestParam(value="duration", defaultValue = "300") long duration) {
        return Mono.just(0L).map(item -> {
            LocalDateTime start = LocalDateTime.now();
            while(Duration.between(start, LocalDateTime.now()).toMillis() < duration) {
                item++;
            }
            return item;
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
