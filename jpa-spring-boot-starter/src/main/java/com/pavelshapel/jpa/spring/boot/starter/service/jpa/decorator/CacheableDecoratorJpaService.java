package com.pavelshapel.jpa.spring.boot.starter.service.jpa.decorator;

import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class CacheableDecoratorJpaService<T extends AbstractEntity> extends AbstractDecoratorJpaService<T> {
    @Autowired
    private CacheManager cacheManager;

    private final Class<T> genericClass;

    @Override
    public T createAndSave() {
        return putToCache(super.createAndSave());
    }

    @Override
    public T save(T entity) {
        return putToCache(super.save(entity));
    }

    @Override
    public T update(Long id, T entity) {
        return putToCache(super.update(id, entity));
    }

    @Override
    public List<T> saveAll(Iterable<T> entities) {
        return super.saveAll(entities).stream()
                .map(this::putToCache).collect(Collectors.toList());
    }

    @Override
    public T findById(Long id) {
        return getFromCache(id).orElseGet(() -> putToCache(super.findById(id)));
    }

    @Override
    public void deleteById(Long id) {
        evictFromCache(id);
        super.deleteById(id);
    }

    @Override
    public void deleteAll() {
        clearCache();
        super.deleteAll();
    }

    private String getCacheName() {
        return genericClass.getSimpleName();
    }

    private Cache getCache() {
        String cacheName = getCacheName();
        return Optional.ofNullable(cacheManager.getCache(cacheName))
                .orElse(new ConcurrentMapCache(cacheName));
    }

    private T putToCache(T entity) {
        getCache().put(entity.getId(), entity);
        return entity;
    }

    private void evictFromCache(Long id) {
        getCache().evict(id);
    }

    private void clearCache() {
        getCache().clear();
    }

    private Optional<T> getFromCache(Long id) {
        return Optional.ofNullable(getCache().get(id, genericClass));
    }
}
