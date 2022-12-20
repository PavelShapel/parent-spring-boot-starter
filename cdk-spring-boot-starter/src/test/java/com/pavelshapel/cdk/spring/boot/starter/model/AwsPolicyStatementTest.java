package com.pavelshapel.cdk.spring.boot.starter.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AwsPolicyStatementTest {

    @Test
    void getResource_ShouldReturnNotNullValue() {
        Arrays.stream(AwsPolicyStatement.values())
                .map(AwsPolicyStatement::getPolicyStatement)
                .forEach(policyStatement -> assertThat(policyStatement).isNotNull());
    }
}