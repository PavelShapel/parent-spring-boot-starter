package com.pavelshapel.core.spring.boot.starter.api.model;

public interface Dto<ID> {
    ID getId();

    void setId(ID id);
}
