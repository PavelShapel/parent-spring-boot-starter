package com.pavelshapel.jpa.spring.boot.starter.service;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.jpa.spring.boot.starter.service.search.SearchCriterion;
import lombok.SneakyThrows;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DaoService<ID, T extends Entity<ID>> {
    @SneakyThrows
    default T create() {
        return getEntityClass().getDeclaredConstructor().newInstance();
    }

    T save(T entity);

    T update(ID id, T entity);

    List<T> saveAll(Iterable<T> entities);


    T findById(ID id);

    List<T> findAllById(Iterable<ID> ids);

    List<T> findAll();

    List<T> findAll(Iterable<SearchCriterion> searchCriteria, Pageable pageable);

    void deleteById(ID id);

    void deleteAll();


    boolean existsById(ID id);

    long getCount();


    List<T> getChildren(T entity);

    List<T> getParentage(ID id);


    Class<T> getEntityClass();
}
