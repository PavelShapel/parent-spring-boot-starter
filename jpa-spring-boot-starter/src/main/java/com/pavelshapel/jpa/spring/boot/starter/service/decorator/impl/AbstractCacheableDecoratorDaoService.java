package com.pavelshapel.jpa.spring.boot.starter.service.decorator.impl;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.jpa.spring.boot.starter.service.decorator.AbstractDecoratorDaoService;
import com.pavelshapel.jpa.spring.boot.starter.service.search.SearchCriterion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;


public abstract class AbstractCacheableDecoratorDaoService<ID, T extends Entity<ID>> extends AbstractDecoratorDaoService<ID, T> {
    @Autowired
    private CacheManager cacheManager;

    @Override
    public T save(T entity) {
        return putToCache(super.save(entity));
    }

    @Override
    public T update(ID id, T entity) {
        return putToCache(super.update(id, entity));
    }

    @Override
    public List<T> saveAll(Iterable<T> entities) {
        return super.saveAll(entities).stream()
                .map(this::putToCache)
                .collect(toList());
    }

    @Override
    public T findById(ID id) {
        return getFromCache(id).orElseGet(() -> putToCache(super.findById(id)));
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        return super.findAllById(ids).stream()
                .map(this::putToCache)
                .collect(toList());
    }

    @Override
    public List<T> findAll() {
        return super.findAll().stream()
                .map(this::putToCache)
                .collect(toList());
    }

    @Override
    public List<T> findAll(Iterable<SearchCriterion> searchCriteria, Pageable pageable) {
        return super.findAll(searchCriteria, pageable).stream()
                .map(this::putToCache)
                .collect(toList());
    }

    @Override
    public void deleteById(ID id) {
        evictFromCache(id);
        super.deleteById(id);
    }

    @Override
    public void deleteAll() {
        clearCache();
        super.deleteAll();
    }

    protected String getCacheName() {
        return getEntityClass().getSimpleName();
    }

    protected Cache getCache() {
        String cacheName = getCacheName();
        return Optional.ofNullable(cacheManager.getCache(cacheName))
                .orElseGet(() -> new ConcurrentMapCache(cacheName));
    }

    protected T putToCache(T entity) {
        getCache().put(entity.getId(), entity);
        return entity;
    }

    protected void evictFromCache(ID id) {
        getCache().evict(id);
    }

    protected void clearCache() {
        getCache().clear();
    }

    protected Optional<T> getFromCache(ID id) {
        return Optional.ofNullable(getCache().get(id, getEntityClass()));
    }
}
