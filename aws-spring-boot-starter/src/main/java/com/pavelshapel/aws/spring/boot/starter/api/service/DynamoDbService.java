package com.pavelshapel.aws.spring.boot.starter.api.service;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.jpa.spring.boot.starter.service.search.SearchCriterion;
import lombok.SneakyThrows;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface DynamoDbService<T extends Entity<String>> {
    @SneakyThrows
    default T create() {
        return getEntityClass().getDeclaredConstructor().newInstance();
    }

    T save(T entity);

    List<T> saveAll(List<T> entities);

    T findById(String id);

    List<T> findAll(Set<SearchCriterion> searchCriteria, Pageable pageable);

    default List<T> findAll(Set<SearchCriterion> searchCriteria) {
        return findAll(searchCriteria, null);
    }

    default List<T> findAll() {
        return findAll(null);
    }

    void deleteById(String id);

    void deleteAllById(Set<String> ids);

    void deleteAll(List<T> entities);

    int getCount();

    Class<T> getEntityClass();

    List<T> getChildren(String id);

    List<T> getParentage(String id);
}
