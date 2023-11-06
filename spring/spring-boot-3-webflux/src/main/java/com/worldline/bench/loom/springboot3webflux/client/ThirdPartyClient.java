package com.worldline.bench.loom.springboot3webflux.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import reactor.core.publisher.Mono;

public interface ThirdPartyClient {

    @GetExchange("/sleep/timed")
    Mono<Void> timed(@RequestParam("time") Long time);
}
