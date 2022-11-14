package com.pavelshapel.aws.spring.boot.starter.provider;

import com.pavelshapel.test.spring.boot.starter.provider.AbstractProvider;

public class OneStringProvider extends AbstractProvider {
    public OneStringProvider() {
        super(String.class);
    }
}