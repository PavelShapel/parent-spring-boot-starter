package com.pavelshapel.core.spring.boot.starter.api.model;

public interface ParentalDto<ID, T extends Dto<ID>> extends Dto<ID> {
    T getParent();

    void setParent(T parent);
}
