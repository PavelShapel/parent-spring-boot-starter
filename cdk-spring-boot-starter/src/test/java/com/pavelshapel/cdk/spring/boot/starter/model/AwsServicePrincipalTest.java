package com.pavelshapel.cdk.spring.boot.starter.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AwsServicePrincipalTest {

    @Test
    void getServicePrincipal_ShouldReturnNotNullValue() {
        Arrays.stream(AwsServicePrincipal.values())
                .map(AwsServicePrincipal::getServicePrincipals)
                .forEach(servicePrincipal -> assertThat(servicePrincipal).isNotNull());
    }
}