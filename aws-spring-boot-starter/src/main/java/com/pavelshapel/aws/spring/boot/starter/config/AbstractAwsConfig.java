package com.pavelshapel.aws.spring.boot.starter.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.client.builder.AwsSyncClientBuilder;
import com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.pavelshapel.aws.spring.boot.starter.properties.nested.AbstractServiceProperties.SERVICE_ENDPOINT_PATTERN;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractAwsConfig<S extends AwsSyncClientBuilder, T> {
    @Autowired
    AwsProperties awsProperties;

    AwsSyncClientBuilder<S, T> clientBuilder;

    protected AbstractAwsConfig(AwsSyncClientBuilder<S, T> clientBuilder) {
        this.clientBuilder = clientBuilder;
    }

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

    protected AwsClientBuilder.EndpointConfiguration createEndpointConfiguration() {
        String serviceEndpoint = String.format(SERVICE_ENDPOINT_PATTERN, awsProperties.getService(), awsProperties.getRegion());
        return new AwsClientBuilder.EndpointConfiguration(serviceEndpoint, awsProperties.getRegion());
    }
}