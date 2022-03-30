package com.pavelshapel.core.spring.boot.starter.impl.model;

import com.pavelshapel.core.spring.boot.starter.api.model.Dto;
import lombok.Data;

@Data
public abstract class AbstractDto<ID> implements Dto<ID> {
    private ID id;
}