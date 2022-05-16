package com.pavelshapel.core.spring.boot.starter.impl.service;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.core.spring.boot.starter.api.model.ParentalEntity;
import com.pavelshapel.core.spring.boot.starter.api.repository.DaoRepository;
import com.pavelshapel.core.spring.boot.starter.api.service.DaoService;
import com.pavelshapel.core.spring.boot.starter.api.util.ClassUtils;
import com.pavelshapel.core.spring.boot.starter.api.util.StreamUtils;
import com.pavelshapel.core.spring.boot.starter.impl.web.search.SearchCriterion;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static org.springframework.util.ReflectionUtils.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractDaoService<ID, T extends Entity<ID>> implements DaoService<ID, T> {
    @Getter(AccessLevel.PROTECTED)
    @Autowired
    DaoRepository<ID, T> daoRepository;
    @Autowired
    ClassUtils classUtils;
    @Autowired
    StreamUtils streamUtils;

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
        classUtils.copyFields(entity, entityFromDatabase);
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
    public List<T> findAll(Collection<SearchCriterion> searchCriteria) {
        Stream<T> stream = findAll().stream();
        return filterStream(searchCriteria, stream)
                .collect(Collectors.toList());
    }

    @Override
    public Stream<T> filterStream(Collection<SearchCriterion> searchCriteria, Stream<T> stream) {
        for (SearchCriterion searchCriterion : searchCriteria) {
            stream = stream.filter(entity -> isApplicableEntity(entity, searchCriterion));
        }
        return stream;
    }

    private boolean isApplicableEntity(T entity, SearchCriterion searchCriterion) {
        return getSearchCriterionPredicate(searchCriterion).test(getFieldValue(entity, searchCriterion));
    }

    private Predicate<Comparable<Object>> getSearchCriterionPredicate(SearchCriterion searchCriterion) {
        return searchCriterion.getOperation().getFunction().apply(searchCriterion.getCastedValue());
    }

    private Comparable<Object> getFieldValue(T entity, SearchCriterion searchCriterion) {
        Field field = findField(entity.getClass(), searchCriterion.getField());
        if (nonNull(field)) {
            makeAccessible(field);
            return ((Comparable<Object>) getField(field, entity));
        } else {
            throw new IllegalArgumentException(searchCriterion.getField());
        }
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
        return Optional.ofNullable(findById(id)).isPresent();
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
        Optional.ofNullable(findById(id))
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
}