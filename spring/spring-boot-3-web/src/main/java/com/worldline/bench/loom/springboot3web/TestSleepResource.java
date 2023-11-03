package com.worldline.bench.loom.springboot3web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestSleepResource {
    
    @GetMapping("/test/virtual")
    public String blocking() throws InterruptedException {
        Thread.sleep(2000);
        return "Hello from Spring Virtual Threads";
    }
}
