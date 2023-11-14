package com.worldline.bench.loom.client;

import java.net.URI;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;

import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import io.helidon.jersey.connector.HelidonConnectorProvider;
import io.helidon.jersey.connector.HelidonProperties;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;

@ApplicationScoped
public class ThirdPartyServiceProvider {

    Logger LOG = Logger.getLogger(ThirdPartyServiceProvider.class.getName());

    @Inject
    @ConfigProperty(name = "sleep-service.maxConnections", defaultValue = "200")
    private int maxConnections;
    
    @ApplicationScoped
    @Produces
    public ThirdPartySleepServiceClient thirdPartySleepServiceClient() {
        var poolingConnectionManager = new PoolingHttpClientConnectionManager();
        poolingConnectionManager.setMaxTotal(1);
        poolingConnectionManager.setDefaultMaxPerRoute(1);

        var builder = RestClientBuilder.newBuilder()
            .register(HelidonConnectorProvider.create())
            .property("jersey.config.apache.client.connectionManager", poolingConnectionManager);

        /*builder.getConfiguration().getProperties()
                .put("jersey.config.apache.client.connectionManager", poolingConnectionManager);*/
        builder.getConfiguration().getProperties().forEach((k, v) -> {
            LOG.info("key: " + k + " value: " + v);
        });

        return builder.baseUri(URI.create("http://localhost:8090"))
        .build(ThirdPartySleepServiceClient.class);
    }

    @ApplicationScoped
    @Produces
    public Client client() {
        var poolingConnectionManager = new PoolingHttpClientConnectionManager();
        poolingConnectionManager.setMaxTotal(maxConnections);
        poolingConnectionManager.setDefaultMaxPerRoute(maxConnections);

        var client = ClientBuilder.newBuilder()
            .property("jersey.config.apache.client.connectionManager", poolingConnectionManager)
            .build();
        
        return client;
    }
}
