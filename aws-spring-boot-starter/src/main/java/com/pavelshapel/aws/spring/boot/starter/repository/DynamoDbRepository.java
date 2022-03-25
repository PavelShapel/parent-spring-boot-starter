package com.pavelshapel.aws.spring.boot.starter.repository;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DynamoDbRepository<ID, T extends Entity<ID>> extends PagingAndSortingRepository<T, ID> {
}