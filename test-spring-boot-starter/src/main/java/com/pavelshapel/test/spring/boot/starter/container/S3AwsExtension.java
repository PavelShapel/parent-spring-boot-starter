package com.pavelshapel.test.spring.boot.starter.container;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

public class S3AwsExtension extends AbstractAwsExtension {
    public S3AwsExtension() {
        super(S3);
    }
}