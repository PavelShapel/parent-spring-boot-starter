package com.pavelshapel.cdk.spring.boot.starter.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AwsResourceTest {

    @Test
    void getResource_ShouldReturnNotNullValue() {
        Arrays.stream(AwsResource.values())
                .map(AwsResource::getResources)
                .forEach(resource -> assertThat(resource).isNotNull());
    }
}