package com.pavelshapel.jpa.spring.boot.starter.repository;

import com.pavelshapel.jpa.spring.boot.starter.entity.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AbstractJpaRepository<ID, T extends Entity<ID>> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}