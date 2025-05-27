package com.krishna.devdrills.controller;

import com.krishna.devdrills.dto.response.QuestionResponse;
import com.krishna.devdrills.service.GeminiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@RestController
public class QuestionController {
    private final GeminiService geminiService;

    public QuestionController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @GetMapping("/question")
    public Mono<List<QuestionResponse>> getQuestion() throws IOException {
        return geminiService.getNextQuestion();
    }
}
