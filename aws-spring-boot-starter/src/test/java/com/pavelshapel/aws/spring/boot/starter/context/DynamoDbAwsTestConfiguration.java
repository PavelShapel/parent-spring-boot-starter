package com.pavelshapel.aws.spring.boot.starter.context;

import com.pavelshapel.aws.spring.boot.starter.annotation.ConditionalOnPropertyDynamoDb;
import com.pavelshapel.aws.spring.boot.starter.api.service.DynamoDbService;
import com.pavelshapel.aws.spring.boot.starter.config.AbstractDynamoDbAwsConfiguration;
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
    @ConditionalOnPropertyDynamoDb
    public UserDynamoDbService userDynamoDbService() {
        return new UserDynamoDbService();
    }

    @Bean
    @ConditionalOnPropertyDynamoDb
    public UserParentalDynamoDBTypeConverter userParentalDynamoDBTypeConverter(DynamoDbService<User> dynamoDbService) {
        return new UserParentalDynamoDBTypeConverter(dynamoDbService);
    }
}