package com.pavelshapel.aws.spring.boot.starter.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.pavelshapel.aws.spring.boot.starter.annotation.ConditionalOnPropertyS3;
import com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties;
import org.springframework.context.annotation.Bean;

public abstract class AbstractS3AwsConfiguration extends AbstractAwsConfiguration<AmazonS3ClientBuilder, AmazonS3> {
    protected AbstractS3AwsConfiguration(AwsProperties awsProperties) {
        super(awsProperties, awsProperties.getS3(), AmazonS3ClientBuilder.standard());
    }

    @Bean
    @ConditionalOnPropertyS3
    public AmazonS3 amazonS3() {
        return buildClient();
    }
}