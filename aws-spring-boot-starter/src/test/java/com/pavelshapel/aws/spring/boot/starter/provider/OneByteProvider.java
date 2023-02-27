package com.pavelshapel.aws.spring.boot.starter.provider;

import com.pavelshapel.test.spring.boot.starter.provider.AbstractProvider;

public class OneByteProvider extends AbstractProvider {
    public OneByteProvider() {
        super(Byte.class);
    }
}