package com.pavelshapel.random.spring.boot.starter.provider;

import com.pavelshapel.test.spring.boot.starter.provider.AbstractLongProvider;

public class Long2Provider extends AbstractLongProvider {
    protected Long2Provider() {
        super(2);
    }
}