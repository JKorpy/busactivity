package com.example.jkorpy.busactivity.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class GtfsConfig {

    private final String subscriptionKey;

    public GtfsConfig(@Value("${security.api-key}") String subscriptionKey) {
        this.subscriptionKey = subscriptionKey;
    }

    @Bean
    public RestClient gtfsRestClient() {
        return RestClient.builder()
                .baseUrl("https://api.nationaltransport.ie/gtfsr/v2")
                .defaultHeader("x-api-key", subscriptionKey)
                .build();
    }
}
