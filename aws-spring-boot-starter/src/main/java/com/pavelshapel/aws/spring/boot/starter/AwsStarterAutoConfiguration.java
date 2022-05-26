package com.pavelshapel.aws.spring.boot.starter;

import com.pavelshapel.aws.spring.boot.starter.annotation.ConditionalOnPropertyDynamoDb;
import com.pavelshapel.aws.spring.boot.starter.annotation.ConditionalOnPropertyS3;
import com.pavelshapel.aws.spring.boot.starter.api.service.BucketHandler;
import com.pavelshapel.aws.spring.boot.starter.api.service.DbHandler;
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

//    @Bean
//    public DbBackup scheduledDbBackup(){
//        return new ScheduledDbBackup();
//    }
}
