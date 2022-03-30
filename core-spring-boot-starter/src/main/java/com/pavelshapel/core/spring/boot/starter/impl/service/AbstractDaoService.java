package com.pavelshapel.core.spring.boot.starter.impl.service;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.core.spring.boot.starter.api.model.ParentalEntity;
import com.pavelshapel.core.spring.boot.starter.api.repository.DaoRepository;
import com.pavelshapel.core.spring.boot.starter.api.service.DaoService;
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
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.springframework.util.ReflectionUtils.makeAccessible;

public abstract class AbstractDaoService<ID, T extends Entity<ID>> implements DaoService<ID, T> {
    @Getter(AccessLevel.PROTECTED)
    @Autowired
    private DaoRepository<ID, T> daoRepository;
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
        return daoRepository.save(entity);
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
        return streamUtils.iterableToList(daoRepository.saveAll(entities));
    }


    @Override
    public T findById(ID id) {
        return daoRepository.findById(id)
                .orElse(null);
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        return streamUtils.iterableToList(daoRepository.findAllById(ids));
    }

    @Override
    public List<T> findAll() {
        return streamUtils.iterableToList(daoRepository.findAll());
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return daoRepository.findAll(pageable);
    }


    @Override
    public void deleteById(ID id) {
        daoRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        daoRepository.deleteAll();
    }


    @Override
    public boolean existsById(ID id) {
        return daoRepository.existsById(id);
    }

    @Override
    public long getCount() {
        return daoRepository.count();
    }

    @Override
    public List<T> getChildren(T entity) {
        return findAll().stream()
                .filter(ParentalEntity.class::isInstance)
                .map(ParentalEntity.class::cast)
                .map(ParentalEntity::getParent)
                .map(parentalEntity -> (T) parentalEntity)
                .filter(entity::equals)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasChildren(T entity) {
        return !getChildren(entity).isEmpty();
    }

    @Override
    public List<T> getParentage(ID id) {
        List<T> result = new ArrayList<>();
        daoRepository.findById(id)
                .filter(ParentalEntity.class::isInstance)
                .filter(result::add)
                .map(ParentalEntity.class::cast)
                .map(ParentalEntity::getParent)
                .map(Entity::getId)
                .ifPresent(parentId -> result.addAll(getParentage((ID) parentId)));
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