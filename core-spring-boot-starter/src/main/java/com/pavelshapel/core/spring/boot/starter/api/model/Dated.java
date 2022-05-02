package com.pavelshapel.core.spring.boot.starter.api.model;

import java.util.Date;

public interface Dated {
    String CREATED_DATE_FIELD = "createdDate";
    String LAST_MODIFIED_DATE_FIELD = "lastModifiedDate";

    Date getCreatedDate();

    void setCreatedDate(Date createdDate);

    Date getLastModifiedDate();

    void setLastModifiedDate(Date lastModifiedDate);
}