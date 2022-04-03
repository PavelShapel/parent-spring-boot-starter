package com.pavelshapel.core.spring.boot.starter.impl.service.decorator.instance;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.core.spring.boot.starter.api.model.ParentalEntity;
import com.pavelshapel.core.spring.boot.starter.impl.service.decorator.AbstractDecoratorSpecificationDaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.joining;

public abstract class AbstractThrowableDecoratorDaoService<ID, T extends Entity<ID>> extends AbstractDecoratorSpecificationDaoService<ID, T> {
    @Override
    public T update(ID id, T entity) {
        verifyId(id);
        return super.update(id, entity);
    }

    @Override
    public T findById(ID id) {
        verifyId(id);
        return super.findById(id);
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
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
    public void deleteById(ID id) {
        verifyId(id);
        verifyChildren(id);
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

    protected void verifyId(ID id) {
        if (isNull(id) || !super.existsById(id)) {
            throw createEntityNotFoundException(singletonList(id));
        }
    }

    protected void verifyCollection(List<T> entities, Iterable<ID> ids) {
        if (entities.isEmpty()) {
            throw createEntityNotFoundException(ids);
        }
    }

    protected void verifyCollection(List<T> entities) {
        verifyCollection(entities, emptyList());
    }

    protected void verifyCount(long count) {
        if (count == 0) {
            throw createEntityNotFoundException(emptyList());
        }
    }

    protected RuntimeException createEntityNotFoundException(Iterable<ID> ids) {
        String stringOfIds = StreamSupport.stream(ids.spliterator(), false)
                .map(String::valueOf)
                .collect(joining(", "));

        return new EntityNotFoundException(
                String.format(
                        "service: [%s]; ids: [%s]",
                        getClass().getSimpleName(),
                        stringOfIds.isEmpty() ? "not defined" : stringOfIds
                )
        );
    }

    private void verifyChildren(ID id) {
        T entity = findById(id);
        String message = String.format("entity [%s] has children", entity);
        if (entity instanceof ParentalEntity && hasChildren(entity)) {
            throw new UnsupportedOperationException(message);
        }
    }

}
