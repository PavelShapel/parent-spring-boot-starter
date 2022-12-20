package com.pavelshapel.cdk.spring.boot.starter.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public enum AwsResource {
    ALL_RESOURCES("*");

    String resource;
}
