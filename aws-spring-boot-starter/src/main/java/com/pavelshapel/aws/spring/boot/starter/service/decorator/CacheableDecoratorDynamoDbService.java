package com.pavelshapel.aws.spring.boot.starter.service.decorator;

import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;


public abstract class CacheableDecoratorDynamoDbService<ID, T extends Entity<ID>> extends AbstractDecoratorDynamoDbService<ID, T> {
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
    public void deleteById(ID id) {
        evictFromCache(id);
        super.deleteById(id);
    }

    @Override
    public void deleteAll() {
        clearCache();
        super.deleteAll();
    }

    private String getCacheName() {
        return getEntityClass().getSimpleName();
    }

    private Cache getCache() {
        String cacheName = getCacheName();
        return Optional.ofNullable(cacheManager.getCache(cacheName))
                .orElseGet(() -> new ConcurrentMapCache(cacheName));
    }

    private T putToCache(T entity) {
        getCache().put(entity.getId(), entity);
        return entity;
    }

    private void evictFromCache(ID id) {
        getCache().evict(id);
    }

    private void clearCache() {
        getCache().clear();
    }

    private Optional<T> getFromCache(ID id) {
        return Optional.ofNullable(getCache().get(id, getEntityClass()));
    }
}
