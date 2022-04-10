package com.pavelshapel.core.spring.boot.starter.api.model;

public interface Named {
    String NAME_FIELD = "name";

    String getName();

    void setName(String name);
}