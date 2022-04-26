package com.pavelshapel.aws.spring.boot.starter;

import com.pavelshapel.aws.spring.boot.starter.config.AwsLambdaConfig;
import com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties;
import com.pavelshapel.aws.spring.boot.starter.util.BucketHandler;
import com.pavelshapel.aws.spring.boot.starter.util.DbHandler;
import com.pavelshapel.aws.spring.boot.starter.util.impl.DynamoDbHandler;
import com.pavelshapel.aws.spring.boot.starter.util.impl.S3BucketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static com.pavelshapel.aws.spring.boot.starter.config.AbstractRequestStreamHandler.LAMBDA;

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
    public BucketHandler s3BucketHandler() {
        return new S3BucketHandler();
    }

    @Bean
    @Profile(LAMBDA)
    public AwsLambdaConfig awsLambdaConfig() {
        return new AwsLambdaConfig();
    }
}
