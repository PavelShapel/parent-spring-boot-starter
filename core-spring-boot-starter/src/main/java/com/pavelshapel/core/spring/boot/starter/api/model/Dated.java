package com.pavelshapel.core.spring.boot.starter.api.model;

import java.util.Date;

public interface Dated {
    Date getCreatedDate();

    void setCreatedDate(Date createdDate);

    Date getLastUpdatedDate();

    void setLastUpdatedDate(Date lastUpdatedDate);
}