package com.worldline.bench.loom.springboot3web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.worldline.bench.loom.springboot3web.client.ThirdPartyClient;

import io.github.mweirauch.micrometer.jvm.extras.ProcessMemoryMetrics;
import io.github.mweirauch.micrometer.jvm.extras.ProcessThreadMetrics;
import io.micrometer.core.instrument.binder.MeterBinder;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@SpringBootApplication
public class Application {

	static Logger logger = LoggerFactory.getLogger(Application.class);

	@Value("${spring.threads.virtual.enabled:false}")
	private boolean isVirtualThreadEnabled;

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

	@EventListener(ContextRefreshedEvent.class)
	public void onStartup() {
		logger.info("Virtual Thread Enabled: {}", isVirtualThreadEnabled);
	}

	@Bean
	public ThirdPartyClient thirdPartyClient() {
		var connectionProvider = ConnectionProvider.create("3rdPartyConnection", maxConnections, true);
		var httpClient = HttpClient.create(connectionProvider);
		var connector = new ReactorClientHttpConnector(httpClient);
		WebClient client = WebClient.builder()
				.clientConnector(connector)
				.baseUrl("http://localhost:8090")
				.build();

		HttpServiceProxyFactory factory = HttpServiceProxyFactory
				.builderFor(WebClientAdapter.create(client))
				.build();
		return factory.createClient(ThirdPartyClient.class);
	}

	/*
	 * @Bean
	 * public PoolingHttpClientConnectionManager poolingConnectionManager() {
	 * Registry<ConnectionSocketFactory> socketFactoryRegistry =
	 * RegistryBuilder.<ConnectionSocketFactory>create()
	 * .register("http", new PlainConnectionSocketFactory()).build();
	 * PoolingHttpClientConnectionManager pollingConnectionManager = new
	 * PoolingHttpClientConnectionManager(socketFactoryRegistry);
	 * pollingConnectionManager.setMaxTotal(500);
	 * pollingConnectionManager.setDefaultMaxPerRoute(500);
	 * return pollingConnectionManager;
	 * }
	 * 
	 * @Bean
	 * public CloseableHttpClient httpClient() {
	 * RequestConfig requestConfig = RequestConfig.custom()
	 * .setConnectionRequestTimeout(Timeout.of(30000, TimeUnit.MILLISECONDS))
	 * .setConnectTimeout(Timeout.of(30000, TimeUnit.MILLISECONDS)).build();
	 * 
	 * return HttpClients.custom()
	 * .setDefaultRequestConfig(requestConfig)
	 * .setConnectionManager(poolingConnectionManager())
	 * .build();
	 * }
	 */

	/*
	 * // Determines the timeout in milliseconds until a connection is established.
	 * private static final int CONNECT_TIMEOUT = 30000;
	 * 
	 * // The timeout when requesting a connection from the connection manager.
	 * private static final int REQUEST_TIMEOUT = 30000;
	 * 
	 * // The timeout for waiting for data
	 * private static final int SOCKET_TIMEOUT = 60000;
	 * 
	 * private static final int MAX_TOTAL_CONNECTIONS = 50;
	 * private static final int DEFAULT_KEEP_ALIVE_TIME_MILLIS = 20 * 1000;
	 * private static final int CLOSE_IDLE_CONNECTION_WAIT_TIME_SECS = 30;
	 * 
	 * @Bean
	 * AsyncTaskExecutor taskExecutor() {
	 * ExecutorService executorService =
	 * Executors.newVirtualThreadPerTaskExecutor();
	 * return new TaskExecutorAdapter(executorService::execute);
	 * }
	 * 
	 * @Bean
	 * TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadsCustomizer()
	 * {
	 * return protocolHandler ->
	 * protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
	 * }
	 * 
	 * public static void main(String[] args) {
	 * SpringApplication.run(SpringBoot3WebApplication.class, args);
	 * }
	 * 
	 * @Bean
	 * public MeterBinder processMemoryMetrics() {
	 * return new ProcessMemoryMetrics();
	 * }
	 * 
	 * @Bean
	 * public MeterBinder processThreadMetrics() {
	 * return new ProcessThreadMetrics();
	 * }
	 * 
	 * @Bean
	 * public PoolingHttpClientConnectionManager poolingConnectionManager() {
	 * Registry<ConnectionSocketFactory> socketFactoryRegistry =
	 * RegistryBuilder.<ConnectionSocketFactory>create()
	 * .register("http", new PlainConnectionSocketFactory()).build();
	 * PoolingHttpClientConnectionManager pollingConnectionManager = new
	 * PoolingHttpClientConnectionManager(socketFactoryRegistry);
	 * pollingConnectionManager.setMaxTotal(500);
	 * pollingConnectionManager.setDefaultMaxPerRoute(500);
	 * return pollingConnectionManager;
	 * }
	 * 
	 * @Bean
	 * public CloseableHttpClient httpClient() {
	 * RequestConfig requestConfig = RequestConfig.custom()
	 * .setConnectionRequestTimeout(Timeout.of(REQUEST_TIMEOUT,
	 * TimeUnit.MILLISECONDS))
	 * .setConnectTimeout(Timeout.of(CONNECT_TIMEOUT,
	 * TimeUnit.MILLISECONDS)).build();
	 * 
	 * return HttpClients.custom()
	 * .setDefaultRequestConfig(requestConfig)
	 * .setConnectionManager(poolingConnectionManager())
	 * .build();
	 * }
	 * 
	 * @Bean
	 * public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
	 * HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new
	 * HttpComponentsClientHttpRequestFactory();
	 * clientHttpRequestFactory.setHttpClient(httpClient());
	 * return clientHttpRequestFactory;
	 * }
	 * 
	 * @Bean
	 * public RestTemplate restTemplate() {
	 * return new RestTemplate(clientHttpRequestFactory());
	 * }
	 */

}
