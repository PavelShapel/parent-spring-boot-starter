package com.pavelshapel.aws.spring.boot.starter.provider;

import com.pavelshapel.test.spring.boot.starter.provider.AbstractProvider;

public class ThreeStringProvider extends AbstractProvider {
    public ThreeStringProvider() {
        super(String.class, String.class, String.class);
    }
}