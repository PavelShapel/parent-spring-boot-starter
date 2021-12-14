package com.pavelshapel.web.spring.boot.starter.web.dto;

import java.io.Serializable;

public interface Dto<ID> extends Serializable {
    ID getId();

    void setId(ID id);
}
