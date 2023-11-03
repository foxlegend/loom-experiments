package com.worldline.bench.loom.springboot3webflux.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class ThirdPartySleepServiceClient {
    
    @Autowired
    private WebClient webClient;

    public Mono<ResponseEntity<Void>> timed(Long time) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/sleep/timed")
                .queryParam("time", time)
                .build()
                )
            .retrieve()
            .toBodilessEntity();
    }
}
