package com.pavelshapel.aws.spring.boot.starter.context;

import com.pavelshapel.aws.spring.boot.starter.config.AbstractS3AwsConfiguration;
import com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3AwsTestConfiguration extends AbstractS3AwsConfiguration {
    @Autowired
    public S3AwsTestConfiguration(AwsProperties awsProperties) {
        super(awsProperties);
    }
}