package com.pavelshapel.jpa.spring.boot.starter.service.jpa;

import com.pavelshapel.core.spring.boot.starter.util.CommonUtils;
import com.pavelshapel.jpa.spring.boot.starter.entity.Entity;
import com.pavelshapel.jpa.spring.boot.starter.repository.AbstractJpaRepository;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.springframework.util.ReflectionUtils.makeAccessible;

public abstract class AbstractJpaService<ID, T extends Entity<ID>> implements JpaService<ID, T> {
    @Getter(AccessLevel.PROTECTED)
    @Autowired
    private AbstractJpaRepository<ID, T> jpaRepository;
    @Autowired
    private CommonUtils commonUtils;

    @Override
    public T createAndSave() {
        return save(create());
    }

    @Override
    public T save(T entity) {
        return jpaRepository.save(entity);
    }

    @Override
    public T update(ID id, T entity) {
        entity.setId(null);
        T entityFromDatabase = findById(id);
        copyFields(entity, entityFromDatabase);
        return jpaRepository.save(entityFromDatabase);
    }

    @Override
    public List<T> saveAll(Iterable<T> entities) {
        return jpaRepository.saveAll(entities);
    }


    @Override
    public T findById(ID id) {
        return jpaRepository.findById(id).get();
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        return jpaRepository.findAllById(ids);
    }

    @Override
    public List<T> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }


    @Override
    public void deleteById(ID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }


    @Override
    public boolean existsById(ID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public long getCount() {
        return jpaRepository.count();
    }

    @Override
    public List<T> findAll(Specification<T> specification) {
        return jpaRepository.findAll(specification);
    }

    @Override
    public Page<T> findAll(Specification<T> specification, Pageable pageable) {
        return jpaRepository.findAll(specification, pageable);
    }

    @Override
    public long getCount(Specification<T> specification) {
        return jpaRepository.count(specification);
    }

    @Override
    public List<T> getParentage(ID id) {
        T child = findById(id);
        T parent;
        List<T> result = new ArrayList<>();
        do {
            result.add(child);
            parent = getParent(child);
            child = parent;
        } while (nonNull(parent));
        return result;
    }

    @Override
    public Class<T> getEntityClass() {
        return (Class<T>) commonUtils.getGenericSuperclass(getClass(), 1)
                .orElseThrow(ClassCastException::new);
    }


    private void copyFields(Object source, Object destination) {
        if (!source.getClass().isAssignableFrom(destination.getClass())) {
            throw new IllegalArgumentException(
                    String.format("destination class [%s] must be same or subclass as source class [%s]",
                            destination.getClass().getName(),
                            source.getClass().getName()
                    )
            );
        } else {
            ReflectionUtils.doWithFields(source.getClass(),
                    field -> {
                        makeAccessible(field);
                        Object value = field.get(source);
                        if (nonNull(value)) {
                            field.set(destination, value);
                        }
                    },
                    field -> !Modifier.isStatic(field.getModifiers()) &&
                            !Modifier.isFinal(field.getModifiers()) &&
                            !Modifier.isTransient(field.getModifiers())

            );
        }
    }
}