package com.pavelshapel.aws.spring.boot.starter.service;

import com.pavelshapel.aws.spring.boot.starter.repository.DynamoDbRepository;
import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.core.spring.boot.starter.api.util.ClassUtils;
import com.pavelshapel.core.spring.boot.starter.api.util.StreamUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.springframework.util.ReflectionUtils.makeAccessible;

public abstract class AbstractDynamoDbService<ID, T extends Entity<ID>> implements DynamoDbService<ID, T> {
    @Getter(AccessLevel.PROTECTED)
    @Autowired
    private DynamoDbRepository<ID, T> dynamoDbRepository;
    @Autowired
    private ClassUtils classUtils;
    @Autowired
    private StreamUtils streamUtils;

    @Override
    public T createAndSave() {
        return save(create());
    }

    @Override
    public T save(T entity) {
        return dynamoDbRepository.save(entity);
    }

    @Override
    public T update(ID id, T entity) {
        entity.setId(null);
        T entityFromDatabase = findById(id);
        copyFields(entity, entityFromDatabase);
        return save(entityFromDatabase);
    }

    @Override
    public List<T> saveAll(Iterable<T> entities) {
        return streamUtils.iterableToList(dynamoDbRepository.saveAll(entities));
    }


    @Override
    public T findById(ID id) {
        return dynamoDbRepository.findById(id)
                .orElse(null);
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        return streamUtils.iterableToList(dynamoDbRepository.findAllById(ids));
    }

    @Override
    public List<T> findAll() {
        return streamUtils.iterableToList(dynamoDbRepository.findAll());
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return dynamoDbRepository.findAll(pageable);
    }


    @Override
    public void deleteById(ID id) {
        dynamoDbRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        dynamoDbRepository.deleteAll();
    }


    @Override
    public boolean existsById(ID id) {
        return dynamoDbRepository.existsById(id);
    }

    @Override
    public long getCount() {
        return dynamoDbRepository.count();
    }

    @Override
    public List<T> getParentage(ID id) {
        T child = findById(id);
        T parent;
        List<T> result = new ArrayList<>();
        do {
            result.add(child);
            parent = getParent(child);
            child = parent;
        } while (nonNull(parent));
        return result;
    }

    @Override
    public Class<T> getEntityClass() {
        return classUtils.getGenericSuperclass(getClass(), 1)
                .map(entityClass -> (Class<T>) entityClass)
                .orElseThrow(UnsupportedOperationException::new);
    }


    private void copyFields(Object source, Object destination) {
        if (isSameOrSubclass(source, destination)) {
            ReflectionUtils.doWithFields(source.getClass(), field -> copyField(source, destination, field), this::filterField);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private boolean isSameOrSubclass(Object source, Object destination) {
        return source.getClass().isAssignableFrom(destination.getClass());
    }

    @SneakyThrows
    private void copyField(Object source, Object destination, Field field) {
        makeAccessible(field);
        Object value = field.get(source);
        if (nonNull(value)) {
            field.set(destination, value);
        }
    }

    private boolean filterField(Field field) {
        return !Modifier.isStatic(field.getModifiers()) &&
                !Modifier.isFinal(field.getModifiers()) &&
                !Modifier.isTransient(field.getModifiers());
    }
}