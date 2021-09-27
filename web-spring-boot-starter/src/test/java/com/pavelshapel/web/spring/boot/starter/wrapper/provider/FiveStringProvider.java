package com.pavelshapel.web.spring.boot.starter.wrapper.provider;


import com.pavelshapel.test.spring.boot.starter.provider.AbstractProvider;

public class FiveStringProvider extends AbstractProvider {
    protected FiveStringProvider() {
        super(String.class, String.class, String.class, String.class, String.class);
    }
}
