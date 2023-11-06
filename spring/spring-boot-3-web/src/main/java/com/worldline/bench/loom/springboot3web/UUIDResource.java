package com.worldline.bench.loom.springboot3web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.worldline.bench.loom.springboot3web.client.ThirdPartyClient;
import com.worldline.bench.loom.springboot3web.entity.UUID;
import com.worldline.bench.loom.springboot3web.repository.UUIDRepository;

@RestController
public class UUIDResource {

    @Autowired
    ThirdPartyClient client;

    @Autowired
    UUIDRepository repository;

    @RequestMapping(value = { "/uuid/platform", "/uuid/virtual" }, produces = "application/json", method = { RequestMethod.GET })
    public UUID uuid(
        @RequestParam(value = "rsCount", defaultValue = "1") int rsCount,
        @RequestParam(value = "rsSleep", defaultValue = "300") long rsSleep,
        @RequestParam(value = "dbCount", defaultValue = "2") int dbCount,
        @RequestParam(value = "dbSleep", defaultValue = "0.025") double dbSleep
        ) throws InterruptedException {
        return simulated(rsCount, rsSleep, dbCount, dbSleep);
    }

    private UUID simulated(int rsCount, long rsSleep, int count, double sleep) throws InterruptedException {
        var uuid = new UUID();
        for (int i = 0; i < Math.max(rsCount, count); i++)  {
            if (i < rsCount) {
                client.timed(rsSleep);
            }
            if (i < count) {
                uuid = repository.generateRandomUUID(sleep);
            }
        }
        return uuid;
    }
}
