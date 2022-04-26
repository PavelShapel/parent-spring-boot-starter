package com.pavelshapel.aws.spring.boot.starter.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public abstract class AbstractAwsConfig {
    @Getter
    @Autowired
    private AwsProperties awsProperties;

    protected AWSCredentialsProvider awsCredentialsProvider() {
        return Optional.ofNullable(awsProperties.getProfile())
                .map(ProfileCredentialsProvider::new)
                .map(AWSCredentialsProvider.class::cast)
                .orElseGet(this::createAwsStaticCredentialsProvider);
    }

    private AWSStaticCredentialsProvider createAwsStaticCredentialsProvider() {
        return new AWSStaticCredentialsProvider(createBasicAWSCredentials());
    }

    private BasicAWSCredentials createBasicAWSCredentials() {
        return new BasicAWSCredentials(awsProperties.getAccessKey(), awsProperties.getSecretKey());
    }

    protected AwsClientBuilder.EndpointConfiguration createEndpointConfiguration(String serviceEndpoint) {
        return new AwsClientBuilder.EndpointConfiguration(serviceEndpoint, awsProperties.getRegion());
    }
}