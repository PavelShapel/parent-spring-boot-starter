package com.pavelshapel.aws.spring.boot.starter.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.client.builder.AwsSyncClientBuilder;
import com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties;
import com.pavelshapel.aws.spring.boot.starter.properties.nested.AbstractServiceProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Optional;

import static com.pavelshapel.aws.spring.boot.starter.properties.nested.AbstractServiceProperties.SERVICE_ENDPOINT_PATTERN;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Getter(AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public abstract class AbstractAwsConfig<S extends AwsSyncClientBuilder, T> {
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
                .map(unused -> clientBuilder.build())
                .orElseGet(this::buildClientWithCredentials);
    }

    private T buildClientWithCredentials() {
        return (T) clientBuilder
                .withRegion(awsProperties.getRegion())
                .withCredentials(awsCredentialsProvider())
                .withEndpointConfiguration(createEndpointConfiguration())
                .build();
    }

    private AwsClientBuilder.EndpointConfiguration createEndpointConfiguration() {
        String serviceEndpoint = String.format(SERVICE_ENDPOINT_PATTERN, serviceProperties.getName(), awsProperties.getRegion());
        return new AwsClientBuilder.EndpointConfiguration(serviceEndpoint, awsProperties.getRegion());
    }
}