package com.pavelshapel.jpa.spring.boot.starter.service;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.jpa.spring.boot.starter.repository.SpecificationDaoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public abstract class AbstractSpecificationDaoService<ID, T extends Entity<ID>> extends AbstractDaoService<ID, T> implements SpecificationDaoService<ID, T> {
    @Override
    public List<T> findAll(Specification<T> specification) {
        return getSpecificationDaoRepository().findAll(specification);
    }

    @Override
    public Page<T> findAll(Specification<T> specification, Pageable pageable) {
        return getSpecificationDaoRepository().findAll(specification, pageable);
    }

    @Override
    public long getCount(Specification<T> specification) {
        return getSpecificationDaoRepository().count(specification);
    }

    private SpecificationDaoRepository<ID, T> getSpecificationDaoRepository() {
        return (SpecificationDaoRepository<ID, T>) getDaoRepository();
    }
}