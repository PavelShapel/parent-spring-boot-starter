package com.pavelshapel.jpa.spring.boot.starter.service.jpa.decorator;

import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;

public abstract class ThrowableDecoratorJpaService<T extends AbstractEntity> extends AbstractDecoratorJpaService<T> {

    @Override
    public T update(Long id, T entity) {
        verifyId(id);
        return super.update(id, entity);
    }

    @Override
    public T findById(Long id) {
        verifyId(id);
        return super.findById(id);
    }

    @Override
    public List<T> findAllById(Iterable<Long> ids) {
        List<T> entities = super.findAllById(ids);
        verifyCollection(entities, ids);
        return entities;
    }

    @Override
    public List<T> findAll() {
        List<T> entities = super.findAll();
        verifyCollection(entities);
        return entities;
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        Page<T> entities = super.findAll(pageable);
        verifyCount(entities.getTotalElements());
        return entities;
    }


    @Override
    public void deleteById(Long id) {
        verifyId(id);
        super.deleteById(id);
    }

    @Override
    public void deleteAll() {
        verifyCount(super.getCount());
        super.deleteAll();
    }

    @Override
    public List<T> findAll(Specification<T> specification) {
        List<T> entities = super.findAll(specification);
        verifyCollection(entities);
        return entities;
    }

    @Override
    public Page<T> findAll(Specification<T> specification, Pageable pageable) {
        Page<T> entities = super.findAll(specification, pageable);
        verifyCount(entities.getTotalElements());
        return entities;
    }

    @Override
    public List<T> getParentage(Long id) {
        List<T> entities = super.getParentage(id);
        verifyCollection(entities);
        return entities;
    }

    protected void verifyId(Long id) {
        if (isNull(id) || !super.existsById(id)) {
            throw createEntityNotFoundException(singletonList(id));
        }
    }

    protected void verifyCollection(List<T> entities, Iterable<Long> ids) {
        if (entities.isEmpty()) {
            throw createEntityNotFoundException(ids);
        }
    }

    protected void verifyCollection(List<T> entities) {
        verifyCollection(entities, emptyList());
    }

    protected void verifyCount(Long count) {
        if (count == 0) {
            throw createEntityNotFoundException(emptyList());
        }
    }

    protected RuntimeException createEntityNotFoundException(Iterable<Long> ids) {
        String stringOfIds = StreamSupport.stream(ids.spliterator(), false)
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        return new EntityNotFoundException(
                String.format(
                        "service: [%s]; ids: [%s]",
                        getClass().getSimpleName(),
                        stringOfIds.isEmpty() ? "empty collection" : stringOfIds
                )
        );
    }
}
