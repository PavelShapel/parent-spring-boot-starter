package com.pavelshapel.cdk.spring.boot.starter.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import software.amazon.awscdk.services.iam.ServicePrincipal;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public enum AwsServicePrincipal {
    LAMBDA(new ServicePrincipal("lambda.amazonaws.com"));

    ServicePrincipal servicePrincipal;
}
