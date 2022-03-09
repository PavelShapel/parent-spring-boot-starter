package com.pavelshapel.core.spring.boot.starter.model;

import java.io.Serializable;

public interface Dto<T> extends Serializable {
    T getId();

    void setId(T id);
}
