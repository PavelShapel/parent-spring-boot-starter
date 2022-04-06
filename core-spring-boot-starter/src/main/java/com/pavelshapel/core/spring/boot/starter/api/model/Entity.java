package com.pavelshapel.core.spring.boot.starter.api.model;

public interface Entity<ID> {
    String MANDATORY = "mandatory";
    String ID = "id";

    ID getId();

    void setId(ID id);
}
