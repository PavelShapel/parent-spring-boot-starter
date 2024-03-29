package com.pavelshapel.aws.spring.boot.starter.config;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.pavelshapel.aws.spring.boot.starter.annotation.ConditionalOnPropertyDynamoDb;
import com.pavelshapel.aws.spring.boot.starter.api.service.ScanExpressionFromSearchCriteriaConverter;
import com.pavelshapel.aws.spring.boot.starter.api.service.TransactionWriteExpressionFromSearchCriteriaConverter;
import com.pavelshapel.aws.spring.boot.starter.impl.service.DynamoDBScanExpressionFromSearchCriteriaConverter;
import com.pavelshapel.aws.spring.boot.starter.impl.service.DynamoDBTransactionWriteExpressionFromSearchCriteriaConverter;
import com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public abstract class AbstractDynamoDbAwsConfiguration extends AbstractAwsConfiguration<AmazonDynamoDBClientBuilder, AmazonDynamoDB> {
    protected AbstractDynamoDbAwsConfiguration(AwsProperties awsProperties) {
        super(awsProperties, awsProperties.getDynamoDb(), AmazonDynamoDBClientBuilder.standard());
    }

    @Bean
    @ConditionalOnPropertyDynamoDb
    public DynamoDBMapperConfig dynamoDBMapperConfig() {
        return DynamoDBMapperConfig.DEFAULT;
    }

    @Bean
    @Primary
    @ConditionalOnPropertyDynamoDb
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(amazonDynamoDB(), dynamoDBMapperConfig());
    }

    @Bean
    @ConditionalOnPropertyDynamoDb
    public AmazonDynamoDB amazonDynamoDB() {
        return buildClient();
    }

    @Bean
    @ConditionalOnPropertyDynamoDb
    public ScanExpressionFromSearchCriteriaConverter scanExpressionFromSearchCriteriaConverter() {
        return new DynamoDBScanExpressionFromSearchCriteriaConverter();
    }

    @Bean
    @ConditionalOnPropertyDynamoDb
    public TransactionWriteExpressionFromSearchCriteriaConverter transactionWriteExpressionFromSearchCriteriaConverter() {
        return new DynamoDBTransactionWriteExpressionFromSearchCriteriaConverter();
    }

    @Bean
    @ConditionalOnPropertyDynamoDb
    public DynamoDB dynamoDB() {
        return new DynamoDB(amazonDynamoDB());
    }
}