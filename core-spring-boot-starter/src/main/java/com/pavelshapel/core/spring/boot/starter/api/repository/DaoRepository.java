package com.pavelshapel.core.spring.boot.starter.api.repository;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;

public interface DaoRepository<ID, T extends Entity<ID>> {
}