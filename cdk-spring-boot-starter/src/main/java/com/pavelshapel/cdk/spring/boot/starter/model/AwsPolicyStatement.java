package com.pavelshapel.cdk.spring.boot.starter.model;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import software.amazon.awscdk.services.iam.Effect;
import software.amazon.awscdk.services.iam.PolicyStatement;

import java.util.function.Supplier;

import static com.pavelshapel.cdk.spring.boot.starter.model.AwsServicePrincipal.ALL;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public enum AwsPolicyStatement {
    LAMBDA_BASIC_EXECUTION(AwsPolicyStatement::createLambdaBasicExecution),
    S3_FULL_ACCESS(AwsPolicyStatement::createS3FullAccess),
    S3_WEBSITE_ACCESS(AwsPolicyStatement::createS3WebsiteAccess);

    Supplier<PolicyStatement> policyStatementSupplier;

    public PolicyStatement getPolicyStatement() {
        return policyStatementSupplier.get();
    }

    private static PolicyStatement createS3FullAccess() {
        return PolicyStatement.Builder.create()
                .effect(Effect.ALLOW)
                .resources(AwsResource.ALL.getResources())
                .actions(AwsActionSet.S3_FULL_ACCESS.getActions())
                .build();
    }

    private static PolicyStatement createS3WebsiteAccess() {
        return PolicyStatement.Builder.create()
                .effect(Effect.ALLOW)
                .principals(ALL.getServicePrincipals())
                .actions(AwsActionSet.S3_GET_OBJECT.getActions())
                .resources(AwsResource.ALL.getResources())
                .build();
    }

    private static PolicyStatement createLambdaBasicExecution() {
        return PolicyStatement.Builder.create()
                .effect(Effect.ALLOW)
                .resources(AwsResource.ALL.getResources())
                .actions(AwsActionSet.LAMBDA_BASIC_EXECUTION.getActions())
                .build();
    }
}
