package com.pavelshapel.core.spring.boot.starter.impl.model;

import com.pavelshapel.core.spring.boot.starter.api.model.ParentalDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractParentalDto<ID> extends AbstractDto<ID> implements ParentalDto<ID> {
    private ID parent;
}