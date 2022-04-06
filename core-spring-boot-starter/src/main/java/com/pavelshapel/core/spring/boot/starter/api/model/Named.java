package com.pavelshapel.core.spring.boot.starter.api.model;

public interface Named {
    public static final String NAME = "name";

    String getName();

    void setName(String name);
}