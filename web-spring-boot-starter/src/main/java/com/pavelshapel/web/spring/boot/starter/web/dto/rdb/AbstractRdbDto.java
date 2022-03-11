package com.pavelshapel.web.spring.boot.starter.web.dto.rdb;

import com.pavelshapel.core.spring.boot.starter.model.Dto;
import lombok.Data;

@Data
public abstract class AbstractRdbDto implements Dto<Long> {
    private Long id;
}