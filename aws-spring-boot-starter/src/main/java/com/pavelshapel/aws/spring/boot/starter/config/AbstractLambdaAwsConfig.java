package com.pavelshapel.aws.spring.boot.starter.config;

import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.pavelshapel.aws.spring.boot.starter.annotation.ConditionalOnPropertyS3;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public abstract class AbstractLambdaAwsConfig extends AbstractAwsConfig {
    @Bean
    @ConditionalOnPropertyS3
    public AmazonS3 amazonS3() {
        return Optional.of(getAwsProperties())
                .filter(properties -> isEmpty(properties.getProfile()))
                .filter(properties -> isEmpty(properties.getAccessKey()))
                .filter(properties -> isEmpty(properties.getSecretKey()))
                .map(unused -> AWSLambdaClientBuilder.defaultClient())
                .orElseGet(this::buildClientWithCredentials);
    }

    private AmazonS3 buildClientWithCredentials() {
        return AWSLambdaClientBuilder
                .standard()
                .withRegion(getAwsProperties().getRegion())
                .withCredentials(awsCredentialsProvider())
                .withEndpointConfiguration(createEndpointConfiguration())
                .build();
    }
}