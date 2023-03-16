package com.pavelshapel.jpa.spring.boot.starter.service;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.core.spring.boot.starter.api.model.ParentalEntity;
import com.pavelshapel.core.spring.boot.starter.api.util.ClassUtils;
import com.pavelshapel.core.spring.boot.starter.enums.PrimitiveType;
import com.pavelshapel.jpa.spring.boot.starter.repository.DaoRepository;
import com.pavelshapel.jpa.spring.boot.starter.service.search.AbstractSearchSpecification;
import com.pavelshapel.jpa.spring.boot.starter.service.search.SearchCriterion;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.pavelshapel.jpa.spring.boot.starter.service.search.SearchOperation.EQUALS;
import static java.util.Collections.singletonList;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Transactional
public abstract class AbstractDaoService<ID, T extends Entity<ID>> implements DaoService<ID, T> {
    @Getter(AccessLevel.PROTECTED)
    @Autowired
    DaoRepository<ID, T> daoRepository;
    @Autowired
    ClassUtils classUtils;
    @Autowired
    ObjectFactory<AbstractSearchSpecification<T>> searchSpecificationFactory;

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
        return Optional.of(entities)
                .map(daoRepository::saveAll)
                .orElseGet(Collections::emptyList);
    }


    @Override
    public T findById(ID id) {
        return daoRepository.findById(id)
                .orElse(null);
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        return Optional.of(ids)
                .map(daoRepository::findAllById)
                .orElseGet(Collections::emptyList);
    }

    @Override
    public List<T> findAll() {
        return Optional.of(daoRepository)
                .map(JpaRepository::findAll)
                .orElseGet(Collections::emptyList);
    }

    @Override
    public List<T> findAll(Iterable<SearchCriterion> searchCriteria, Pageable pageable) {
        return daoRepository.findAll(getSearchSpecification(searchCriteria), pageable).getContent();
    }

    private AbstractSearchSpecification<T> getSearchSpecification(Iterable<SearchCriterion> searchCriteria) {
        AbstractSearchSpecification<T> searchSpecification = searchSpecificationFactory.getObject();
        searchSpecification.setSearchCriteria(searchCriteria);
        return searchSpecification;
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
        SearchCriterion searchCriterion = SearchCriterion.builder()
                .field(ParentalEntity.PARENT_FIELD)
                .operation(EQUALS)
                .value(String.format("%s<%s>", entity.getId().toString(), PrimitiveType.STRING.name()))
                .build();
        return findAll(singletonList(searchCriterion), Pageable.unpaged());
    }

    @Override
    public List<T> getParentage(ID id) {
        List<T> parents = new ArrayList<>();
        Optional.ofNullable(findById(id))
                .filter(ParentalEntity.class::isInstance)
                .filter(parents::add)
                .map(ParentalEntity.class::cast)
                .map(ParentalEntity::getParent)
                .map(Entity::getId)
                .ifPresent(parentId -> parents.addAll(getParentage((ID) parentId)));
        return parents;
    }

    @Override
    public Class<T> getEntityClass() {
        return classUtils.getGenericSuperclass(getClass(), 1)
                .map(entityClass -> (Class<T>) entityClass)
                .orElseThrow(UnsupportedOperationException::new);
    }
}