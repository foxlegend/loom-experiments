package com.worldline.bench.loom.springboot3web;

import java.time.Duration;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SleepResource {

    Logger LOG = LoggerFactory.getLogger(SleepResource.class);

    @RequestMapping(value = { "/sleep/platform", "/sleep/virtual" }, method = { RequestMethod.GET })
    public Long blocking(@RequestParam(name = "duration", defaultValue = "300") long duration) throws InterruptedException {
        LOG.debug("SLEEP - thread: [{}]", Thread.currentThread());
        return this.sleep(duration);
    }

    private Long sleep(long duration) throws InterruptedException{
        var start = LocalDateTime.now();
        Thread.sleep(duration);
        return Duration.between(start, LocalDateTime.now()).toMillis();
    }
}
