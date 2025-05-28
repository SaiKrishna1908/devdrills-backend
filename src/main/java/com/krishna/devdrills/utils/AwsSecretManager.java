//package com.krishna.devdrills.utils;
//
// import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
// import software.amazon.awssdk.regions.Region;
// import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
// import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
// import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
//
//
//public class AwsSecretManager {
//
//    public static String getSecret(String secretName) {
//
//        Region region = Region.of("us-east-2");
//
//
//        SecretsManagerClient client = SecretsManagerClient.builder()
//                .credentialsProvider(DefaultCredentialsProvider.create())
//                .region(region)
//                .build();
//
//        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
//                .secretId(secretName)
//                .build();
//
//        GetSecretValueResponse getSecretValueResponse;
//        getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
//        return getSecretValueResponse.secretString();
//    }
//}
