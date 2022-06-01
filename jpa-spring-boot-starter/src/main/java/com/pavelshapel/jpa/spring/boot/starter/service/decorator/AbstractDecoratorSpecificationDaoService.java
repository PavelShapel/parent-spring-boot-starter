package com.pavelshapel.jpa.spring.boot.starter.service.decorator;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.jpa.spring.boot.starter.service.SpecificationDaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;


public abstract class AbstractDecoratorSpecificationDaoService<ID, T extends Entity<ID>> extends AbstractDecoratorDaoService<ID, T> implements SpecificationDaoService<ID, T> {
    @Override
    public List<T> findAll(Specification<T> specification) {
        return getSpecificationWrapped().findAll(specification);
    }

    @Override
    public Page<T> findAll(Specification<T> specification, Pageable pageable) {
        return getSpecificationWrapped().findAll(specification, pageable);
    }

    @Override
    public long getCount(Specification<T> specification) {
        return getSpecificationWrapped().getCount(specification);
    }

    private SpecificationDaoService<ID, T> getSpecificationWrapped() {
        return (SpecificationDaoService<ID, T>) getWrapped();
    }
}
