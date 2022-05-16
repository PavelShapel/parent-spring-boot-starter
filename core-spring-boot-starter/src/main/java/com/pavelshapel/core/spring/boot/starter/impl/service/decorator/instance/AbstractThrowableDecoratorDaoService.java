package com.pavelshapel.core.spring.boot.starter.impl.service.decorator.instance;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.core.spring.boot.starter.api.model.ParentalEntity;
import com.pavelshapel.core.spring.boot.starter.impl.service.decorator.AbstractDecoratorSpecificationDaoService;
import com.pavelshapel.core.spring.boot.starter.impl.web.search.SearchCriterion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.pavelshapel.core.spring.boot.starter.impl.web.search.SearchOperation.IS_NULL;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.joining;

public abstract class AbstractThrowableDecoratorDaoService<ID, T extends Entity<ID>> extends AbstractDecoratorSpecificationDaoService<ID, T> {
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
    public Page<T> findAll(Pageable pageable) {
        Page<T> entities = super.findAll(pageable);
        verifyCount(entities.getTotalElements());
        return entities;
    }

    @Override
    public List<T> findAll(Collection<SearchCriterion> searchCriteria) {
        List<T> entities = super.findAll(searchCriteria);
        verifyCollection(entities);
        return entities;
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
        if (entity instanceof ParentalEntity && hasChildren(entity)) {
            throw new UnsupportedOperationException(message);
        }
    }

    private void verifyRootExists(ID id, T entity) {
        Optional.ofNullable(entity)
                .filter(ParentalEntity.class::isInstance)
                .map(ParentalEntity.class::cast)
                .filter(parentalEntity -> isNull(parentalEntity.getParent()) && rootExists(id))
                .ifPresent(unused -> throwRootAlreadyExistsException());
    }

    private void throwRootAlreadyExistsException() {
        throw new UnsupportedOperationException("root already exists");
    }

    private boolean rootExists(ID id) {
        SearchCriterion searchCriterion = new SearchCriterion();
        searchCriterion.setField(ParentalEntity.PARENT_FIELD);
        searchCriterion.setOperation(IS_NULL);
        return super.findAll(singletonList(searchCriterion)).stream()
                .findFirst()
                .map(Entity::getId)
                .map(rootId -> Objects.equals(id, rootId))
                .filter(Boolean.FALSE::equals)
                .isPresent();
    }
}
