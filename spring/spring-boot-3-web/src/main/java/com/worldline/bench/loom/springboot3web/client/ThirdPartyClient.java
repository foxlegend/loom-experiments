package com.worldline.bench.loom.springboot3web.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface ThirdPartyClient {

    @GetExchange("/sleep/timed")
    void timed(@RequestParam("time") Long time);
}
