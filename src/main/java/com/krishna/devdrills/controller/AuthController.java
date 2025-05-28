package com.krishna.devdrills.controller;

import com.krishna.devdrills.dto.request.RegisterRequest;
import com.krishna.devdrills.dto.request.SignInRequest;
import com.krishna.devdrills.dto.response.LoginResponse;
import com.krishna.devdrills.dto.response.RegisterResponse;
import com.krishna.devdrills.service.CognitoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final CognitoService cognitoService;

    @PostMapping("/register")
    public Mono<RegisterResponse> register(@RequestBody RegisterRequest request) {
        return cognitoService
                        .registerUser(request.getName(),
                                request.getUsername(),
                                request.getPassword(),
                                request.getEmail());
    }

    @PostMapping("/signin")
    public Mono<LoginResponse> signIn(@RequestBody SignInRequest request) {
        return cognitoService
                        .signIn(request.getUsername(), request.getPassword());

    }
}