package com.pavelshapel.web.spring.boot.starter.wrapper.provider;


import com.pavelshapel.test.spring.boot.starter.provider.AbstractProvider;

public class ThreeStringProvider extends AbstractProvider {
    protected ThreeStringProvider() {
        super(String.class, String.class, String.class);
    }
}
