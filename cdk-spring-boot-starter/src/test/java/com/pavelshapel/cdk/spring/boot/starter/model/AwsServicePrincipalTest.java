package com.pavelshapel.cdk.spring.boot.starter.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AwsServicePrincipalTest {

    @Test
    void getServicePrincipal_ShouldReturnNotNullValue() {
        Arrays.stream(AwsServicePrincipal.values())
                .map(AwsServicePrincipal::getServicePrincipal)
                .forEach(servicePrincipal -> assertThat(servicePrincipal).isNotNull());
    }
}