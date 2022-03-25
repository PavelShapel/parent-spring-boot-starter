package com.pavelshapel.core.spring.boot.starter.api.model;

import java.io.Serializable;

public interface Dto<ID> extends Serializable {
    ID getId();

    void setId(ID id);
}
