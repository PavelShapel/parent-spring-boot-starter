package com.pavelshapel.aws.spring.boot.starter.config;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.pavelshapel.aws.spring.boot.starter.annotation.ConditionalOnPropertyLambda;
import com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties;
import org.springframework.context.annotation.Bean;

public abstract class AbstractLambdaAwsConfig extends AbstractAwsConfig<AWSLambdaClientBuilder, AWSLambda> {
    protected AbstractLambdaAwsConfig(AwsProperties awsProperties) {
        super(awsProperties, awsProperties.getLambda(), AWSLambdaClientBuilder.standard());
    }

    @Bean
    @ConditionalOnPropertyLambda
    public AWSLambda awsLambda() {
        return buildClient();
    }
}