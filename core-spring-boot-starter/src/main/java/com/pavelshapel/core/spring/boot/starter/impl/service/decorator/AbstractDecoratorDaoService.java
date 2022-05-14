package com.pavelshapel.core.spring.boot.starter.impl.service.decorator;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.core.spring.boot.starter.api.service.DaoService;
import com.pavelshapel.core.spring.boot.starter.impl.web.search.SearchCriterion;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractDecoratorDaoService<ID, T extends Entity<ID>> implements DaoService<ID, T> {
    DaoService<ID, T> wrapped;

    @Override
    public T create() {
        return wrapped.create();
    }

    @Override
    public T createAndSave() {
        return wrapped.createAndSave();
    }

    @Override
    public T save(T entity) {
        return wrapped.save(entity);
    }

    @Override
    public T update(ID id, T entity) {
        return wrapped.update(id, entity);
    }

    @Override
    public List<T> saveAll(Iterable<T> entities) {
        return wrapped.saveAll(entities);
    }


    @Override
    public T findById(ID id) {
        return wrapped.findById(id);
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        return wrapped.findAllById(ids);
    }

    @Override
    public List<T> findAll() {
        return wrapped.findAll();
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return wrapped.findAll(pageable);
    }

    @Override
    public List<T> findAll(List<SearchCriterion> searchCriteria) {
        return wrapped.findAll(searchCriteria);
    }


    @Override
    public void deleteById(ID id) {
        wrapped.deleteById(id);
    }

    @Override
    public void deleteAll() {
        wrapped.deleteAll();
    }


    @Override
    public boolean existsById(ID id) {
        return wrapped.existsById(id);
    }

    @Override
    public long getCount() {
        return wrapped.getCount();
    }

    @Override
    public List<T> getChildren(T entity) {
        return wrapped.getChildren(entity);
    }

    @Override
    public boolean hasChildren(T entity) {
        return wrapped.hasChildren(entity);
    }

    @Override
    public List<T> getParentage(ID id) {
        return wrapped.getParentage(id);
    }

    @Override
    public Class<T> getEntityClass() {
        return wrapped.getEntityClass();
    }
}
