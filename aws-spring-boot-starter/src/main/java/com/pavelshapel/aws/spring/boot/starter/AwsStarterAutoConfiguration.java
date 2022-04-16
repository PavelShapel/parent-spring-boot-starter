package com.pavelshapel.aws.spring.boot.starter;

import com.pavelshapel.aws.spring.boot.starter.config.AwsLambdaConfig;
import com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties;
import com.pavelshapel.aws.spring.boot.starter.util.DbHandler;
import com.pavelshapel.aws.spring.boot.starter.util.DynamoDbHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsStarterAutoConfiguration {
    public static final String TYPE = "aws";

    @Bean
    public AwsContextRefreshedListener awsContextRefreshedListener() {
        return new AwsContextRefreshedListener();
    }

    @Bean
    public AwsProperties awsProperties() {
        return new AwsProperties();
    }

    @Bean
    public DbHandler dynamoDbHandler() {
        return new DynamoDbHandler();
    }

    @Bean
    public AwsLambdaConfig awsLambdaConfig() {
        return new AwsLambdaConfig();
    }
}
