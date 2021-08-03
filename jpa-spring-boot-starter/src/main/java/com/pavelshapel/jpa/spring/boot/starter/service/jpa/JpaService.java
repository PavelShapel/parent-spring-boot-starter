package com.pavelshapel.jpa.spring.boot.starter.service.jpa;

import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface JpaService<T extends AbstractEntity> {
    T create();

    T createAndSave();

    T save(T entity);

    T update(Long id, T entity);

    List<T> saveAll(Iterable<T> entities);


    T findById(Long id);

    List<T> findAllById(Iterable<Long> ids);

    List<T> findAll();

    Page<T> findAll(Pageable pageable);


    void deleteById(Long id);

    void deleteAll();


    boolean existsById(Long id);

    long getCount();


    List<T> findAll(Specification<T> specification);

    Page<T> findAll(Specification<T> specification, Pageable pageable);

    long getCount(Specification<T> specification);
}
