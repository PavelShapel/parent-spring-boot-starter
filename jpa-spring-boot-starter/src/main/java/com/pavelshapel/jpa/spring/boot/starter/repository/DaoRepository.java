package com.pavelshapel.jpa.spring.boot.starter.repository;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DaoRepository<ID, T extends Entity<ID>> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}