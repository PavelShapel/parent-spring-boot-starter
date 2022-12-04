package com.pavelshapel.aws.spring.boot.starter.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.client.builder.AwsSyncClientBuilder;
import com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties;
import com.pavelshapel.aws.spring.boot.starter.properties.nested.AbstractServiceProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Getter(AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@EnableConfigurationProperties(AwsProperties.class)
public abstract class AbstractAwsConfiguration<S extends AwsSyncClientBuilder, T> {
    final AwsProperties awsProperties;
    final AbstractServiceProperties serviceProperties;
    final AwsSyncClientBuilder<S, T> clientBuilder;

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

    protected T buildClient() {
        return Optional.of(awsProperties)
                .filter(properties -> isEmpty(properties.getProfile()))
                .filter(properties -> isEmpty(properties.getAccessKey()))
                .filter(properties -> isEmpty(properties.getSecretKey()))
                .map(unused -> buildClientWithoutCredentials())
                .orElseGet(this::buildClientWithCredentials);
    }

    private T buildClientWithoutCredentials() {
        return buildClientCredentials(DefaultAWSCredentialsProviderChain.getInstance());
    }

    private T buildClientWithCredentials() {
        return buildClientCredentials(awsCredentialsProvider());
    }

    private T buildClientCredentials(AWSCredentialsProvider awsCredentialsProvider) {
        return (T) clientBuilder
                .withCredentials(awsCredentialsProvider)
                .withEndpointConfiguration(createEndpointConfiguration())
                .build();
    }

    private AwsClientBuilder.EndpointConfiguration createEndpointConfiguration() {
        return new AwsClientBuilder.EndpointConfiguration(serviceProperties.getEndpoint(), awsProperties.getRegion());
    }
}