package com.worldline.bench.loom.springboot3web;

import java.io.Closeable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.socket.ConnectionSocketFactory;
import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.core5.http.config.Registry;
import org.apache.hc.core5.http.config.RegistryBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import io.github.mweirauch.micrometer.jvm.extras.ProcessMemoryMetrics;
import io.github.mweirauch.micrometer.jvm.extras.ProcessThreadMetrics;
import io.micrometer.core.instrument.binder.MeterBinder;

@SpringBootApplication
@EnableFeignClients
public class SpringBoot3WebApplication {

	// Determines the timeout in milliseconds until a connection is established.
    private static final int CONNECT_TIMEOUT = 30000;
     
    // The timeout when requesting a connection from the connection manager.
    private static final int REQUEST_TIMEOUT = 30000;
     
    // The timeout for waiting for data
    private static final int SOCKET_TIMEOUT = 60000;
 
    private static final int MAX_TOTAL_CONNECTIONS = 50;
    private static final int DEFAULT_KEEP_ALIVE_TIME_MILLIS = 20 * 1000;
    private static final int CLOSE_IDLE_CONNECTION_WAIT_TIME_SECS = 30;

	@Bean
	AsyncTaskExecutor taskExecutor() {
		ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
		return new TaskExecutorAdapter(executorService::execute);
	}

	@Bean
	TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadsCustomizer() {
		return protocolHandler -> protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot3WebApplication.class, args);
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
	public PoolingHttpClientConnectionManager poolingConnectionManager() {
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", new PlainConnectionSocketFactory()).build();
		PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		pollingConnectionManager.setMaxTotal(500);
		pollingConnectionManager.setDefaultMaxPerRoute(500);
		return pollingConnectionManager;
	}

	@Bean
	public CloseableHttpClient httpClient() {
		RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.of(REQUEST_TIMEOUT, TimeUnit.MILLISECONDS))
                .setConnectTimeout(Timeout.of(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)).build();
 
        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(poolingConnectionManager())
                .build();
	}

	@Bean
	public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setHttpClient(httpClient());
		return clientHttpRequestFactory;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate(clientHttpRequestFactory());
	}

}
