package com.pavelshapel.web.spring.boot.starter.web.dto.rds;

import com.pavelshapel.web.spring.boot.starter.web.dto.Dto;
import lombok.Data;

@Data
public abstract class AbstractRdsDto implements Dto<Long> {
    private Long id;
}