package com.pavelshapel.aws.spring.boot.starter.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.pavelshapel.aws.spring.boot.starter.annotation.ConditionalOnPropertyDynamoDb;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public abstract class AbstractDynamoDbAwsConfig extends AbstractAwsConfig {
    @Bean
    @ConditionalOnPropertyDynamoDb
    public DynamoDBMapperConfig dynamoDBMapperConfig() {
        return DynamoDBMapperConfig.DEFAULT;
    }

    @Bean
    @Primary
    @ConditionalOnPropertyDynamoDb
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(amazonDynamoDB(), dynamoDBMapperConfig());
    }

    @Bean
    @ConditionalOnPropertyDynamoDb
    public AmazonDynamoDB amazonDynamoDB() {
        return Optional.of(getAwsProperties())
                .filter(properties -> isEmpty(properties.getProfile()))
                .filter(properties -> isEmpty(properties.getAccessKey()))
                .filter(properties -> isEmpty(properties.getSecretKey()))
                .map(unused -> AmazonDynamoDBClientBuilder.defaultClient())
                .orElseGet(this::buildClientWithCredentials);
    }

    private AmazonDynamoDB buildClientWithCredentials() {
        return AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(getAwsProperties().getRegion())
                .withCredentials(awsCredentialsProvider())
                .withEndpointConfiguration(createEndpointConfiguration())
                .build();
    }
}