package com.pavelshapel.cdk.spring.boot.starter.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public enum AwsActionSet {
    S3_FULL_ACCESS(List.of("s3:*", "s3-object-lambda:*")),
    S3_GET_OBJECT(List.of("s3:GetObject")),
    LAMBDA_BASIC_EXECUTION(List.of("logs:CreateLogGroup", "logs:CreateLogStream", "logs:PutLogEvents"));

    List<String> actions;
}
