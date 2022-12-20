package com.pavelshapel.cdk.spring.boot.starter.model;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;

import java.util.List;
import java.util.function.Supplier;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public enum AwsPolicyStatement {
    LAMBDA_BASIC_EXECUTION(AwsPolicyStatement::createLambdaBasicExecution),
    S3_FULL_ACCESS(AwsPolicyStatement::createS3FullAccess);

    Supplier<PolicyStatement> policyStatementSupplier;

    public PolicyStatement getPolicyStatement() {
        return policyStatementSupplier.get();
    }

    private static PolicyStatement createS3FullAccess() {
        return PolicyStatement.Builder.create()
                .effect(Effect.ALLOW)
                .resources(List.of(AwsResource.ALL_RESOURCES.getResource()))
                .actions(AwsActionSet.S3_FULL_ACCESS.getActions())
                .build();
    }

    private static PolicyStatement createLambdaBasicExecution() {
        return PolicyStatement.Builder.create()
                .effect(Effect.ALLOW)
                .resources(List.of(AwsResource.ALL_RESOURCES.getResource()))
                .actions(AwsActionSet.LAMBDA_BASIC_EXECUTION.getActions())
                .build();
    }
}
