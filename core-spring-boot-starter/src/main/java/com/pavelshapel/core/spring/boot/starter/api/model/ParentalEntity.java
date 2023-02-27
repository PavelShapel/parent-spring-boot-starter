package com.pavelshapel.core.spring.boot.starter.api.model;

public interface ParentalEntity<ID, T extends ParentalEntity<ID, T>> extends Entity<ID> {
    String PARENT_FIELD = "parent";
    String ROOT_ID = String.format("%s#null", PARENT_FIELD);

    T getParent();

    void setParent(T parent);
}
