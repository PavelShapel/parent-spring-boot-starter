package com.pavelshapel.aws.spring.boot.starter.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

public abstract class AbstractDynamoDbConfig {
    @Autowired
    private AwsProperties awsProperties;

    public AWSCredentialsProvider awsCredentialsProvider() {
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

    @Bean
    public DynamoDBMapperConfig dynamoDBMapperConfig() {
        return DynamoDBMapperConfig.DEFAULT;
    }

    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(amazonDynamoDB(), dynamoDBMapperConfig());
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder
                .standard()
                .withCredentials(awsCredentialsProvider())
                .withEndpointConfiguration(createEndpointConfiguration())
                .build();
    }

    private AwsClientBuilder.EndpointConfiguration createEndpointConfiguration() {
        return new AwsClientBuilder.EndpointConfiguration(awsProperties.getEndpoint(), awsProperties.getRegion());
    }
}