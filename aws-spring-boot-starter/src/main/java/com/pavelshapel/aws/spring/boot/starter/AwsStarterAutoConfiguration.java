package com.pavelshapel.aws.spring.boot.starter;

import com.pavelshapel.aws.spring.boot.starter.annotation.ConditionalOnPropertyDynamoDb;
import com.pavelshapel.aws.spring.boot.starter.annotation.ConditionalOnPropertyLambda;
import com.pavelshapel.aws.spring.boot.starter.annotation.ConditionalOnPropertyS3;
import com.pavelshapel.aws.spring.boot.starter.api.service.BucketHandler;
import com.pavelshapel.aws.spring.boot.starter.api.service.DbHandler;
import com.pavelshapel.aws.spring.boot.starter.api.service.RequestHandler;
import com.pavelshapel.aws.spring.boot.starter.api.service.ResponseHandler;
import com.pavelshapel.aws.spring.boot.starter.impl.service.ApiGatewayRequestHandler;
import com.pavelshapel.aws.spring.boot.starter.impl.service.ApiGatewayResponseHandler;
import com.pavelshapel.aws.spring.boot.starter.impl.service.DynamoDbHandler;
import com.pavelshapel.aws.spring.boot.starter.impl.service.S3BucketHandler;
import com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties;
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
    @ConditionalOnPropertyS3
    @ConditionalOnPropertyDynamoDb
    @ConditionalOnPropertyLambda
    public AwsProperties awsProperties() {
        return new AwsProperties();
    }

    @Bean
    @ConditionalOnPropertyDynamoDb
    public DbHandler dynamoDbHandler() {
        return new DynamoDbHandler();
    }

    @Bean
    @ConditionalOnPropertyS3
    public BucketHandler s3BucketHandler() {
        return new S3BucketHandler();
    }

    @Bean
    public ResponseHandler responseHandler() {
        return new ApiGatewayResponseHandler();
    }

    @Bean
    public RequestHandler requestHandler() {
        return new ApiGatewayRequestHandler();
    }
}
