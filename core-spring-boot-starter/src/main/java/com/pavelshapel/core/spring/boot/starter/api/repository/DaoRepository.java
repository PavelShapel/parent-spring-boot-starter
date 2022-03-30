package com.pavelshapel.core.spring.boot.starter.api.repository;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DaoRepository<ID, T extends Entity<ID>> extends PagingAndSortingRepository<T, ID> {
}