package com.pavelshapel.jpa.spring.boot.starter.entity;

import java.io.Serializable;

public interface Entity<ID> extends Serializable {
    ID getId();

    void setId(ID id);
}
