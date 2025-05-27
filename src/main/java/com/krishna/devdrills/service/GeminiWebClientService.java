package com.krishna.devdrills.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class GeminiWebClientService {
    private final WebClient webClient = WebClient.create("https://generativelanguage.googleapis.com");

    @Value("${google.ai.api-key}") // Store your API key securely (e.g., environment variable, Spring Cloud Config)
    private String apiKey;

    public Mono<String> callGemini(String prompt) {
        String endpoint = "/v1beta/models/gemini-2.0-flash:generateContent";

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))
                )
        );

        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path(endpoint).queryParam("key", apiKey).build())
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class);
    }
}