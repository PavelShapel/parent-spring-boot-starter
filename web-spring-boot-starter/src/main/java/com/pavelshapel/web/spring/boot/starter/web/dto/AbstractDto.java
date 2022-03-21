package com.pavelshapel.web.spring.boot.starter.web.dto;

import com.pavelshapel.core.spring.boot.starter.model.Dto;
import lombok.Data;

@Data
public abstract class AbstractDto implements Dto<Long> {
    private Long id;
}