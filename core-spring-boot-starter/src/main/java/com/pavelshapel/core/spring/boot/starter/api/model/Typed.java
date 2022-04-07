package com.pavelshapel.core.spring.boot.starter.api.model;

public interface Typed {
    String TYPE = "type";

    String getType();

    void setType(String type);
}