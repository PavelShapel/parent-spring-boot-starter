package com.pavelshapel.aws.spring.boot.starter.api.service;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.jpa.spring.boot.starter.service.search.SearchCriterion;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface DynamoDbService<T extends Entity<String>> {
    T save(T entity);

    List<T> saveAll(List<T> entities);

    T findById(String id);

    List<T> findAll(Set<SearchCriterion> searchCriteria, Pageable pageable);

    void deleteById(String id);

    Class<T> getEntityClass();
}
