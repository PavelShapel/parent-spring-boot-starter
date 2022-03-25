package com.pavelshapel.core.spring.boot.starter.api.model;

import java.io.Serializable;

public interface Entity<ID> extends Serializable {
    String MANDATORY = "mandatory";

    ID getId();

    void setId(ID id);
}
