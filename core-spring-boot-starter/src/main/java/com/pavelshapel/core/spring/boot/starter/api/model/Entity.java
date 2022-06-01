package com.pavelshapel.core.spring.boot.starter.api.model;

public interface Entity<ID> extends Comparable<Entity<ID>> {
    String MANDATORY = "mandatory";
    String ID_FIELD = "id";

    ID getId();

    void setId(ID id);
}
