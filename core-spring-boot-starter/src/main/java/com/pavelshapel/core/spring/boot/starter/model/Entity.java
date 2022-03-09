package com.pavelshapel.core.spring.boot.starter.model;

import java.io.Serializable;

public interface Entity<T> extends Serializable {
    T getId();

    void setId(T id);
}
