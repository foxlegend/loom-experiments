package com.worldline.bench.loom.springboot3web.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "third-party-sleep-service")
public interface ThirdPartySleepServiceClient {
    
    @RequestMapping(method = RequestMethod.GET, value = "/sleep/timed")
    void timed(@RequestParam("time") Long time);
}
