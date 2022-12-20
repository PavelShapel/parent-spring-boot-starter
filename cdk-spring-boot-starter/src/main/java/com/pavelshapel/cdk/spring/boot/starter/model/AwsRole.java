package com.pavelshapel.cdk.spring.boot.starter.model;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import software.amazon.awscdk.services.iam.Role;
import software.constructs.Construct;

import java.util.function.BiFunction;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public enum AwsRole {
    LAMBDA_BASIC_EXECUTION(AwsRole::createLambdaBasicExecution),
    LAMBDA_S3_ALL_ACTIONS(AwsRole::createS3FullAccess);

    BiFunction<Construct, String, Role> roleBiFunction;

    public Role getRole(Construct scope, String id) {
        return roleBiFunction.apply(scope, id);
    }

    private static Role createS3FullAccess(Construct scope, String id) {
        Role role = createLambdaRole(scope, id);
        role.addToPolicy(AwsPolicyStatement.LAMBDA_BASIC_EXECUTION.getPolicyStatement());
        role.addToPolicy(AwsPolicyStatement.S3_FULL_ACCESS.getPolicyStatement());
        return role;
    }

    private static Role createLambdaBasicExecution(Construct scope, String id) {
        Role role = createLambdaRole(scope, id);
        role.addToPolicy(AwsPolicyStatement.LAMBDA_BASIC_EXECUTION.getPolicyStatement());
        return role;
    }

    private static Role createLambdaRole(Construct scope, String id) {
        return Role.Builder.create(scope, id)
                .assumedBy(AwsServicePrincipal.LAMBDA.getServicePrincipal())
                .build();
    }
}
