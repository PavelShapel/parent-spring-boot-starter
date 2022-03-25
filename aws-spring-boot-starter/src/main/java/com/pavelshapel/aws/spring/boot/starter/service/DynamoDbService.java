package com.pavelshapel.aws.spring.boot.starter.service;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DynamoDbService<ID, T extends Entity<ID>> {
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


    T getParent(T entity);

    List<T> getParentage(ID id);

    Class<T> getEntityClass();
}
