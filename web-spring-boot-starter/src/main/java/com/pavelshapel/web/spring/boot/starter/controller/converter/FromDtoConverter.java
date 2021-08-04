package com.pavelshapel.web.spring.boot.starter.controller.converter;

import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;

public abstract class FromDtoConverter<T extends AbstractEntity> implements DtoConverter<T> {
}
