package com.pavelshapel.random.spring.boot.starter.randomizer.model.bounded;

import com.pavelshapel.core.spring.boot.starter.api.util.ClassUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;

@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractBoundedType<T> implements BoundedType<T> {
    @Autowired
    ClassUtils classUtils;

    @Override
    public Class<T> getType() {
        return classUtils.getGenericSuperclass(getClass())
                .map(this::castClass)
                .orElseThrow(IllegalArgumentException::new);
    }

    private Class<T> castClass(Class<?> targetClass) {
        return (Class<T>) targetClass;
    }
}
