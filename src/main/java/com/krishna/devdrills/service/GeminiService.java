package com.krishna.devdrills.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krishna.devdrills.dto.response.QuestionResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeminiService {

    private final ObjectMapper objectMapper;
    private final GeminiWebClientService geminiWebClientService;

    @Value("${app.ai.prompt}")
    private String systemPrompt;

    public GeminiService(GeminiWebClientService webClient) {
        this.objectMapper = new ObjectMapper();
        this.geminiWebClientService = webClient;
    }

    public Mono<List<QuestionResponse>> getNextQuestion() throws IOException {

        var response = geminiWebClientService.callGemini(systemPrompt);

        return response.handle((s, sink) -> {
            try {
                String validJson = extractJson(s);
                sink.next(objectMapper.readValue(validJson, new TypeReference<List<QuestionResponse>>() {})
                        .stream()
                        .peek(questionResponse -> questionResponse.setOptions(List.of(
                                questionResponse.getOption1(),
                                questionResponse.getOption2(),
                                questionResponse.getOption3(),
                                questionResponse.getOption4()
                        ))).toList());
            } catch (Exception e) {
                sink.error(new RuntimeException("Failed to parse JSON", e));
            }
        });
    }

    String extractJson(String geminiResponse) throws JsonProcessingException {

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(geminiResponse);
            JsonNode candidates = root.get("candidates");

            if (candidates != null && candidates.isArray() && !candidates.isEmpty()) {
                JsonNode firstCandidate = candidates.get(0);
                JsonNode content = firstCandidate.get("content");
                if (content != null) {
                    JsonNode parts = content.get("parts");
                    if (parts != null && parts.isArray() && !parts.isEmpty()) {
                        JsonNode firstPart = parts.get(0);
                        JsonNode textNode = firstPart.get("text");
                        if (textNode != null && textNode.isTextual()) {
                            String text = textNode.asText();
                            // Check if the text starts and ends with ```json and extract the inner part
                            if (text.startsWith("```json") && text.endsWith("```")) {
                                return text.substring("```json".length(), text.length() - "```".length()).trim();
                            }
                            // If it doesn't have the ```json block, return as is (might need further processing)
                            return text.trim();
                        }
                    }
                }
            }
            return null;
    }
}
