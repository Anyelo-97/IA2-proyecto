package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {

    @Value("${rutaia.n8n.webhook-secret}")
    private String n8nWebhookSecret;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .defaultHeader("X-Webhook-Secret", n8nWebhookSecret)
                .build();
    }
}

