package com.worldline.bench.loom.springboot3webflux;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import io.github.mweirauch.micrometer.jvm.extras.ProcessMemoryMetrics;
import io.github.mweirauch.micrometer.jvm.extras.ProcessThreadMetrics;
import io.micrometer.core.instrument.binder.MeterBinder;

@SpringBootApplication
@EnableAutoConfiguration
public class SpringBoot3WebfluxApplication {

	@Value("${thirdparty.sleep.service.url:test}")
	private String thirdPartySleepServiceUrl;

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot3WebfluxApplication.class, args);
	}

	@Bean
	public WebClient webClient() {
		return WebClient.builder()
				.baseUrl(thirdPartySleepServiceUrl)
				.build();
	}

	@Bean
	public MeterBinder processMemoryMetrics() {
		return new ProcessMemoryMetrics();
	}

	@Bean
	public MeterBinder processThreadMetrics() {
		return new ProcessThreadMetrics();
	}

}
