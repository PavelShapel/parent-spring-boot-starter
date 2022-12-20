package com.pavelshapel.cdk.spring.boot.starter.model;


import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AwsActionSetTest {

    @Test
    void getActions_ShouldReturnNotNullValue() {
        Arrays.stream(AwsActionSet.values())
                .map(AwsActionSet::getActions)
                .forEach(actions -> assertThat(actions).asList().isNotEmpty());
    }
}