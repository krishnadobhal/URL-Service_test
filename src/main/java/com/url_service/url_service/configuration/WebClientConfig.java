package com.url_service.url_service.configuration;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class WebClientConfig {

    @Value("${RandomIDURL}")
    private String baseUrl;

    @Bean
    public WebClient webClient() {
        String cleanedUrl = baseUrl.replaceAll("^\"|\"$", "").trim();
        System.out.println("WebClient base URL: " + cleanedUrl);
        return WebClient.builder()
                .baseUrl(cleanedUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
