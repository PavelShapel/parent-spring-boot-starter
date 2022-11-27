package com.pavelshapel.jpa.spring.boot.starter.repository;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DaoRepository<ID, T extends Entity<ID>> extends PagingAndSortingRepository<T, ID> {
    @Query("select t from #{#entityName} t where t.parent = ?1")
    List<T> getChildren(T entity);
}