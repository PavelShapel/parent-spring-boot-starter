package com.pavelshapel.core.spring.boot.starter.api.model;

public interface ParentalDto<ID> extends Dto<ID> {
    ID getParent();

    void setParent(ID parent);
}
