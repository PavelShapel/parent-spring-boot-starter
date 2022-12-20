package com.pavelshapel.cdk.spring.boot.starter.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AwsResourceTest {

    @Test
    void getResource_ShouldReturnNotNullValue() {
        Arrays.stream(AwsResource.values())
                .map(AwsResource::getResource)
                .forEach(resource -> assertThat(resource).isNotNull());
    }
}