package com.pavelshapel.core.spring.boot.starter.api.model;

import java.util.Date;

public interface Dated {
    String CREATED_DATE = "createdDate";
    String LAST_MODIFIED_DATE = "lastModifiedDate";

    Date getCreatedDate();

    void setCreatedDate(Date createdDate);

    Date getLastModifiedDate();

    void setLastModifiedDate(Date lastModifiedDate);
}