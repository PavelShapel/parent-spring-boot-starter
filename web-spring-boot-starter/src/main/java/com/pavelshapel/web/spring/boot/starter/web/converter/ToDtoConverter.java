package com.pavelshapel.web.spring.boot.starter.web.converter;

import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;

public abstract class ToDtoConverter<T extends AbstractEntity> implements DtoConverter<T> {
}
