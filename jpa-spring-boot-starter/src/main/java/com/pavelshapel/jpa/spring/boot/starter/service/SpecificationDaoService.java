package com.pavelshapel.jpa.spring.boot.starter.service;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface SpecificationDaoService<ID, T extends Entity<ID>> extends DaoService<ID, T> {
    List<T> findAll(Specification<T> specification);

    Page<T> findAll(Specification<T> specification, Pageable pageable);

    long getCount(Specification<T> specification);
}
