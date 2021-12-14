package com.pavelshapel.jpa.spring.boot.starter.service.jpa;

import com.pavelshapel.jpa.spring.boot.starter.entity.Entity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface JpaService<ID, T extends Entity<ID>> {
    T create();

    T createAndSave();

    T save(T entity);

    T update(ID id, T entity);

    List<T> saveAll(Iterable<T> entities);


    T findById(ID id);

    List<T> findAllById(Iterable<ID> ids);

    List<T> findAll();

    Page<T> findAll(Pageable pageable);


    void deleteById(ID id);

    void deleteAll();


    boolean existsById(ID id);

    long getCount();


    List<T> findAll(Specification<T> specification);

    Page<T> findAll(Specification<T> specification, Pageable pageable);

    long getCount(Specification<T> specification);


    T getParent(T entity);

    List<T> getParentage(ID id);

    Class<T> getEntityClass();
}
