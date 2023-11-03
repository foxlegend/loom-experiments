package com.worldline.bench.loom.springboot3web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.worldline.bench.loom.springboot3web.client.ThirdPartySleepServiceClient;
import com.worldline.bench.loom.springboot3web.repository.UUIDRepository;

@RestController
public class UUIDResource {

    @Autowired
    ThirdPartySleepServiceClient client;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UUIDRepository repository;

    @GetMapping(value = "/uuid", produces = "application/json")
    public com.worldline.bench.loom.springboot3web.entity.UUID uuid(
        @RequestParam(value = "rsCount", defaultValue = "1") int rsCount,
        @RequestParam(value = "rsSleep", defaultValue = "300") long rsSleep,
        @RequestParam(value = "dbCount", defaultValue = "1") int dbCount,
        @RequestParam(value = "dbSleep", defaultValue = "0.025") double dbSleep
        ) {
        simulated(rsCount, rsSleep, dbCount, dbSleep);
        return repository.generateRandomUUID(0.025);
    }

    private void simulated(int rsCount, long rsSleep, int count, double sleep) {
        for (int i = 0; i < Math.max(rsCount, count); i++)  {
            if (i < rsCount) {
                // client.timed(rsSleep);
                doCall(rsSleep);
            }
            if (i < count) {
                repository.generateRandomUUID(sleep);
            }
        }
    }

    private void doCall(long rsSleep) {
        String urlTemplate = UriComponentsBuilder.fromHttpUrl("http://localhost:8000/sleep/timed")
            .queryParam("time", rsSleep)
            .encode()
            .toUriString();
        restTemplate.exchange(urlTemplate, HttpMethod.GET, HttpEntity.EMPTY, Void.class);
    }
}
