package com.pavelshapel.aws.spring.boot.starter;

public class MaxRetryCountException extends RuntimeException {
    public MaxRetryCountException(long retryCount) {
        super(String.format("maximum number of retries [%d]", retryCount));
    }
}
