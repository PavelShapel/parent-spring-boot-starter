package com.pavelshapel.jpa.spring.boot.starter.service.decorator;

import com.pavelshapel.core.spring.boot.starter.model.Entity;
import com.pavelshapel.jpa.spring.boot.starter.service.JpaService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;


public abstract class AbstractDecoratorJpaService<ID, T extends Entity<ID>> implements JpaService<ID, T> {
    @Getter(AccessLevel.PROTECTED)
    @Setter(AccessLevel.PROTECTED)
    private JpaService<ID, T> wrapped;

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
    public List<T> findAll(Specification<T> specification) {
        return wrapped.findAll(specification);
    }

    @Override
    public Page<T> findAll(Specification<T> specification, Pageable pageable) {
        return wrapped.findAll(specification, pageable);
    }

    @Override
    public long getCount(Specification<T> specification) {
        return wrapped.getCount();
    }

    @Override
    public T getParent(T entity) {
        return wrapped.getParent(entity);
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
