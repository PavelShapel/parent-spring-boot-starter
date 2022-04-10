package com.pavelshapel.core.spring.boot.starter.api.model;

public interface Typed<T> {
    String TYPE_FIELD = "type";

    T getType();

    void setType(T type);
}