package com.krishna.devdrills.service;

import com.krishna.devdrills.config.CognitoProperties;
import com.krishna.devdrills.dto.response.LoginResponse;
import com.krishna.devdrills.dto.response.RegisterResponse;
import com.krishna.devdrills.utils.CognitoSecretHash;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CognitoService {

    private final CognitoProperties cognitoProperties;

    private CognitoIdentityProviderClient getClient() {
        return CognitoIdentityProviderClient.builder()
                .region(Region.of(cognitoProperties.getRegion()))
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    public Mono<RegisterResponse> registerUser(String name, String username, String password, String email) {
        CognitoIdentityProviderClient client = getClient();
        try {
            SignUpRequest signUpRequest = SignUpRequest.builder()
                    .clientId(cognitoProperties.getClientId())
                    .username(username)
                    .password(password)
                    .secretHash(CognitoSecretHash.calculateSecretHash(cognitoProperties.getClientId(), cognitoProperties.getClientSecret(), username))
                    .userAttributes(AttributeType.builder().name("email").value(email).build(),
                            AttributeType.builder().name("preferred_username").value(username).build(),
                            AttributeType.builder().name("name").value(name).build())
                    .build();
            try {
                SignUpResponse response = client.signUp(signUpRequest);
                RegisterResponse registerResponse = new RegisterResponse(response.userSub());
                return Mono.just(registerResponse);
            } catch (Exception e) {
                throw new RuntimeException("Error during user registration: ");
            }
        } catch (UsernameExistsException e) {
            throw new RuntimeException("User already exists");
        }
    }

    public Mono<LoginResponse> signIn(String username, String password) {
        return Mono.fromCallable(() -> {
            CognitoIdentityProviderClient client = getClient();
            Map<String, String> authParams = new HashMap<>();
            authParams.put("USERNAME", username);
            authParams.put("PASSWORD", password);
            authParams.put("SECRET_HASH", CognitoSecretHash.calculateSecretHash(
                    cognitoProperties.getClientId(),
                    cognitoProperties.getClientSecret(),
                    username));

            InitiateAuthRequest authRequest = InitiateAuthRequest.builder()
                    .clientId(cognitoProperties.getClientId())
                    .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                    .authParameters(authParams)
                    .build();
            try {
                InitiateAuthResponse response = client.initiateAuth(authRequest);
                var accessToken = response.authenticationResult().accessToken();
                return new LoginResponse(accessToken);
            } catch (NotAuthorizedException e) {
                throw new RuntimeException("Invalid username or password");
            } catch (Exception e) {
                throw new RuntimeException("Error during sign-in: " + e.getMessage(), e);
            }
        });
    }
}

