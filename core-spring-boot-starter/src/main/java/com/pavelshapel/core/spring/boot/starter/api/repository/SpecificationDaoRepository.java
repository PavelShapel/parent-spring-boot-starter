package com.pavelshapel.core.spring.boot.starter.api.repository;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SpecificationDaoRepository<ID, T extends Entity<ID>> extends DaoRepository<ID, T>, JpaSpecificationExecutor<T> {
}