package com.pavelshapel.aws.spring.boot.starter.provider;

import com.pavelshapel.test.spring.boot.starter.provider.AbstractProvider;

public class TwoStringProvider extends AbstractProvider {
    public TwoStringProvider() {
        super(String.class, String.class);
    }
}