package com.pavelshapel.jpa.spring.boot.starter.service.decorator.impl;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.core.spring.boot.starter.api.model.ParentalEntity;
import com.pavelshapel.jpa.spring.boot.starter.service.decorator.AbstractDecoratorDaoService;
import com.pavelshapel.jpa.spring.boot.starter.service.search.SearchCriterion;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.pavelshapel.jpa.spring.boot.starter.service.search.SearchOperation.IS_NULL;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.joining;

public abstract class AbstractThrowableDecoratorDaoService<ID, T extends Entity<ID>> extends AbstractDecoratorDaoService<ID, T> {
    @Override
    public T save(T entity) {
        verifyRootExists(null, entity);
        return super.save(entity);
    }

    @Override
    public T update(ID id, T entity) {
        verifyId(id);
        verifyRootExists(id, entity);
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
    public List<T> findAll(Iterable<SearchCriterion> searchCriteria, Pageable pageable) {
        List<T> entities = super.findAll(searchCriteria, pageable);
        verifyCount(entities.size());
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

    protected void verifyId(ID id) {
        if (isNull(id) || !super.existsById(id)) {
            throwEntityNotFoundException(singletonList(id));
        }
    }

    protected void verifyCollection(List<T> entities, Iterable<ID> ids) {
        if (entities.isEmpty()) {
            throwEntityNotFoundException(ids);
        }
    }

    protected void verifyCollection(List<T> entities) {
        verifyCollection(entities, emptyList());
    }

    protected void verifyCount(long count) {
        if (count == 0) {
            throwEntityNotFoundException(emptyList());
        }
    }

    protected void throwEntityNotFoundException(Iterable<ID> ids) {
        String stringOfIds = StreamSupport.stream(ids.spliterator(), false)
                .map(String::valueOf)
                .collect(joining(", "));

        throw new EntityNotFoundException(
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
        if (entity instanceof ParentalEntity && !getChildren(entity).isEmpty()) {
            throw new UnsupportedOperationException(message);
        }
    }

    private void verifyRootExists(ID id, T entity) {
        Optional.ofNullable(entity)
                .filter(ParentalEntity.class::isInstance)
                .map(ParentalEntity.class::cast)
                .filter(parentalEntity -> isNull(parentalEntity.getParent()) && rootExists(id))
                .ifPresent(this::throwRootAlreadyExistsException);
    }

    private boolean rootExists(ID id) {
        SearchCriterion searchCriterion = SearchCriterion.builder()
                .field(ParentalEntity.PARENT_FIELD)
                .operation(IS_NULL)
                .build();
        return super.findAll(singletonList(searchCriterion), Pageable.unpaged()).stream()
                .findFirst()
                .map(Entity::getId)
                .map(rootId -> Objects.equals(id, rootId))
                .filter(Boolean.FALSE::equals)
                .isPresent();
    }

    private void throwRootAlreadyExistsException(ParentalEntity<?, ?> entity) {
        throw new UnsupportedOperationException(String.format("root [%s] already exists", entity.toString()));
    }
}
