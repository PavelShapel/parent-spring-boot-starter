package com.pavelshapel.core.spring.boot.starter.api.service;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.core.spring.boot.starter.impl.web.search.SearchCriterion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DaoService<ID, T extends Entity<ID>> {
    T create();

    T createAndSave();

    T save(T entity);

    T update(ID id, T entity);

    List<T> saveAll(Iterable<T> entities);


    T findById(ID id);

    List<T> findAllById(Iterable<ID> ids);

    List<T> findAll();

    Page<T> findAll(Pageable pageable);

    List<T> findAll(List<SearchCriterion> searchCriteria);


    void deleteById(ID id);

    void deleteAll();


    boolean existsById(ID id);

    long getCount();


    List<T> getChildren(T entity);

    boolean hasChildren(T entity);

    List<T> getParentage(ID id);


    Class<T> getEntityClass();
}
