package com.pavelshapel.core.spring.boot.starter.impl.util;

import com.pavelshapel.core.spring.boot.starter.api.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static org.springframework.util.ReflectionUtils.*;

public class CoreClassUtils implements ClassUtils {
    @Override
    public Optional<Class<?>> getGenericSuperclass(Class<?> sourceClass) {
        return getGenericSuperclass(sourceClass, 0);
    }

    @Override
    public Optional<Class<?>> getGenericSuperclass(Class<?> sourceClass, int index) {
        try {
            return Optional.ofNullable(sourceClass)
                    .map(Class::getGenericSuperclass)
                    .filter(ParameterizedType.class::isInstance)
                    .map(ParameterizedType.class::cast)
                    .map(ParameterizedType::getActualTypeArguments)
                    .map(types -> types[index])
                    .map(type -> (Class<?>) type);
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    @Override
    public void copyFields(Object source, Object destination) {
        doWithFields(
                source.getClass(),
                sourceField -> copyField(source, destination, sourceField),
                this::filterField
        );
    }

    private void copyField(Object source, Object destination, Field sourceField) {
        makeAccessible(sourceField);
        Object value = getField(sourceField, source);
        Optional.ofNullable(findField(destination.getClass(), sourceField.getName()))
                .filter(destinationField -> filterField(destinationField) && nonNull(value))
                .ifPresent(destinationField -> {
                    makeAccessible(destinationField);
                    setField(destinationField, destination, value);
                });
    }

    private boolean filterField(Field field) {
        return !Modifier.isStatic(field.getModifiers()) &&
                !Modifier.isFinal(field.getModifiers()) &&
                !Modifier.isTransient(field.getModifiers());
    }

    @Override
    public Object getFieldValue(String fieldName, Object entity) {
        return Optional.ofNullable(fieldName)
                .filter(StringUtils::hasText)
                .filter(unused -> nonNull(entity))
                .map(name -> findField(entity.getClass(), name))
                .map(this::makeFieldAccessible)
                .map(field -> getField(field, entity))
                .orElseThrow();
    }

    private Field makeFieldAccessible(Field field) {
        makeAccessible(field);
        return field;
    }
}
