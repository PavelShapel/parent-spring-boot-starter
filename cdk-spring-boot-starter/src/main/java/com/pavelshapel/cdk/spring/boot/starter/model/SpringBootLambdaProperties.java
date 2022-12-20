package com.pavelshapel.cdk.spring.boot.starter.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import software.amazon.awscdk.services.iam.Role;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.logs.RetentionDays;
import software.constructs.Construct;

@Value
@Builder(builderMethodName = "internalBuilder")
public class SpringBootLambdaProperties {
    public static final String HTTP_API_URL_PATTERN = "http api url for [%s]";

    @NonNull
    Construct scope;
    @NonNull
    String assetReference;
    @NonNull
    Role role;
    @Builder.Default
    Runtime runtime = Runtime.JAVA_11;
    @Builder.Default
    String handler = "org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest";
    @Builder.Default
    int memorySize = 1024;
    @Builder.Default
    int timeOut = 30;
    @Builder.Default
    int reservedConcurrentExecutions = 2;
    @Builder.Default
    RetentionDays retention = RetentionDays.ONE_WEEK;

    public static SpringBootLambdaPropertiesBuilder builder(Construct scope, String assetReference, Role role) {
        return internalBuilder()
                .scope(scope)
                .assetReference(assetReference)
                .role(role);
    }
}

