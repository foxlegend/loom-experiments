package com.worldline.bench.loom.springboot3web;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CPUResource {

    @GetMapping(value = { "/cpu/platform", "/cpu/virtual" })
    public Long cpu(@RequestParam(value = "duration", defaultValue = "300") long duration) {
        Long loopCount = 0L;
        LocalDateTime start = LocalDateTime.now();
        while (Duration.between(start, LocalDateTime.now()).toMillis() < duration) {
            loopCount++;
        }
        return loopCount;
    }
}
