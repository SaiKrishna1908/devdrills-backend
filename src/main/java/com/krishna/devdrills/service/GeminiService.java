package com.krishna.devdrills.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.krishna.devdrills.dto.response.QuestionResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeminiService {

    private final ObjectMapper objectMapper;
    private final GeminiWebClientService geminiWebClientService;


    public GeminiService(GeminiWebClientService webClient) {
        this.objectMapper = new ObjectMapper();
        this.geminiWebClientService = webClient;
    }

    public Mono<List<QuestionResponse>> getNextQuestion() throws IOException {
        String prompt = "Imagine you are a senior software developer and you have to give accessing a junior software developers knowledge by giving them real world scenarios where algorithms and DSA used.\n" +
                "\n" +
                "\n" +
                "You have to give them a read world SITUATION and the dev should be able to solve that situation with a action that requires a use of fundamental Comouter Science Algorithm or a Data Structure. Explain the SITUATION in detail with no less than 100 characters\n" +
                "\n" +
                "\n" +
                "The Situation should be of such a kind that the dev should be able to answer it in 2 - 5 words.\n" +
                "\n" +
                "\n" +
                "Also give a short description on how this algorithm can be useful and give examples of popular frameworks use this algorithm at its core.\n" +
                "\n" +
                "\n" +
                "For example:\n" +
                "\n" +
                "\n" +
                "A simple situation would look like this\n" +
                "\n" +
                "\n" +
                "Your team is building a feature to quickly find a specific product by its unique ID from a massive list. What algorithm comes to mind?\n" +
                "\n" +
                "So the expected answer should be something like this\n" +
                "\n" +
                " I will sort the products by uniqueId while insertion I will maintain this order and use binary search for quick search." +
                "\n" +
                "Your response MUST be a valid JSON array of objects, where each object has the following keys: 'situation' (string), 'expectedAnswer' (string), 'notes' (string), and 'examples' (array of strings). Do not include any other text or explanations outside of this JSON structure.\n" +
                "\n" +
                "For example, a single JSON object in the array should look like this:\n" +
                "{\n" +
                "  \\\"situation\\\": \\\"Detailed Explanation here\\\",\n" +
                "  \\\"expectedAnswer\\\": \\\"Binary Search\\\",\n" +
                "  \\\"notes\\\": \\\"Detailed Notes here\\\",\n" +
                "  \\\"examples\\\": [\\\"Real world frameworks which use this framework and how they use it\\\"]\n" +
                "  \\\"option1\\\": \\\"Option 1\\\",\n" +
                "  \\\"option2\\\": \\\"Option 2\\\",\n" +
                "  \\\"option3\\\": \\\"Option 3\\\",\n" +
                "  \\\"option4\\\": \\\"Option 4\\\",\n" +
                "}\n" +
                "\n" +
                "Generate a JSON array containing 5 such objects.";

        var response = geminiWebClientService.callGemini(prompt);

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
