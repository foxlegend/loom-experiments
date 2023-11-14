package com.worldline.bench.loom.springboot3webflux;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.worldline.bench.loom.springboot3webflux.client.ThirdPartyClient;

import io.github.mweirauch.micrometer.jvm.extras.ProcessMemoryMetrics;
import io.github.mweirauch.micrometer.jvm.extras.ProcessThreadMetrics;
import io.micrometer.core.instrument.binder.MeterBinder;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@SpringBootApplication
@EnableAutoConfiguration
public class Application {

	@Value("${thirdparty.sleep.service.url:http\\://localhost\\:8090}")
	private String thirdPartySleepServiceUrl;

	@Value("${thirdparty-sleep-service.maxConnections:500}")
	private int maxConnections;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public MeterBinder processMemoryMetrics() {
		return new ProcessMemoryMetrics();
	}

	@Bean
	public MeterBinder processThreadMetrics() {
		return new ProcessThreadMetrics();
	}

	@Bean
	public ThirdPartyClient thirdPartyClient() {
		var connectionProvider = ConnectionProvider.create("3rdPartyConnection", maxConnections, true);
		var httpClient = HttpClient.create(connectionProvider);
		var connector = new ReactorClientHttpConnector(httpClient);
		WebClient client = WebClient.builder()
				.clientConnector(connector)
				.baseUrl(thirdPartySleepServiceUrl)
				.build();

		HttpServiceProxyFactory factory = HttpServiceProxyFactory
				.builderFor(WebClientAdapter.create(client))
				.build();
		return factory.createClient(ThirdPartyClient.class);
	}

}
