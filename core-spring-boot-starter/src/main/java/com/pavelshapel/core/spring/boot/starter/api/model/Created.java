package com.pavelshapel.core.spring.boot.starter.api.model;

public interface Created {
    String CREATED_BY_FIELD = "createdBy";
    String LAST_MODIFIED_BY_FIELD = "lastModifiedBy";

    String getCreatedBy();

    void setCreatedBy(String user);

    String getLastModifiedBy();

    void setLastModifiedBy(String user);
}
