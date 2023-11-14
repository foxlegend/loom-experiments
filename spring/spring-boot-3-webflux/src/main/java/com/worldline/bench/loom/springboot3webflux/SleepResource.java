package com.worldline.bench.loom.springboot3webflux;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class SleepResource {

    @GetMapping(value = "/sleep/reactive")
    public Mono<Long> sleep(@RequestParam(value = "duration", defaultValue = "300") long duration) {
        return Mono.just(LocalDateTime.now())
                .delayElement(Duration.ofMillis(duration))
                .map(item -> Duration.between(item, LocalDateTime.now()).toMillis());
    }
}
