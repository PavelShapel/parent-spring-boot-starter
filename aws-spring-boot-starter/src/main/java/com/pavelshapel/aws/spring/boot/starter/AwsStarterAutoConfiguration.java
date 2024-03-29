package com.pavelshapel.aws.spring.boot.starter;

import com.pavelshapel.aws.spring.boot.starter.annotation.ConditionalOnPropertyS3;
import com.pavelshapel.aws.spring.boot.starter.api.service.BucketHandler;
import com.pavelshapel.aws.spring.boot.starter.api.service.RequestHandler;
import com.pavelshapel.aws.spring.boot.starter.api.service.ResponseHandler;
import com.pavelshapel.aws.spring.boot.starter.impl.service.ApiGatewayRequestHandler;
import com.pavelshapel.aws.spring.boot.starter.impl.service.ApiGatewayResponseHandler;
import com.pavelshapel.aws.spring.boot.starter.impl.service.S3BucketHandler;
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
