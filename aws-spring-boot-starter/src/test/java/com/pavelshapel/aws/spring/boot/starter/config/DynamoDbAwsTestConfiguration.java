package com.pavelshapel.aws.spring.boot.starter.config;

import com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDbAwsTestConfiguration extends AbstractDynamoDbAwsConfiguration {
    @Autowired
    public DynamoDbAwsTestConfiguration(AwsProperties awsProperties) {
        super(awsProperties);
    }

    @Bean
    public UserDynamoDbService userDynamoDbService() {
        return new UserDynamoDbService();
    }
}