package com.pavelshapel.jpa.spring.boot.starter.repository;

import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbstractJpaRepository<T extends AbstractEntity> extends JpaRepository<T, Long> {
}