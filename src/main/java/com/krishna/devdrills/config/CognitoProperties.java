package com.krishna.devdrills.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "aws.cognito")
public class CognitoProperties {
    private String region;
    private String clientId;
    private String userPoolId;
    private String clientSecret;
    private String domain;
    private String redirectUri;
}