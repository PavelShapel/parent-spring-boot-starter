package com.pavelshapel.jpa.spring.boot.starter.service.jpa;

import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import com.pavelshapel.jpa.spring.boot.starter.repository.AbstractJpaRepository;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Modifier;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.springframework.util.ReflectionUtils.makeAccessible;

public abstract class AbstractJpaService<T extends AbstractEntity> implements JpaService<T> {
    @Getter(AccessLevel.PROTECTED)
    @Autowired
    private AbstractJpaRepository<T> jpaRepository;

    @Override
    public T createAndSave() {
        return save(create());
    }

    @Override
    public T save(T entity) {
        return jpaRepository.save(entity);
    }

    @Override
    public T update(Long id, T entity) {
        T entityFromDatabase = findById(id);
        copyFields(entity, entityFromDatabase);
        return jpaRepository.save(entityFromDatabase);
    }

    @Override
    public List<T> saveAll(Iterable<T> entities) {
        return jpaRepository.saveAll(entities);
    }


    @Override
    public T findById(Long id) {
        return jpaRepository.findById(id).get();
    }

    @Override
    public List<T> findAllById(Iterable<Long> ids) {
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
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        jpaRepository.deleteAll();
    }


    @Override
    public boolean existsById(Long id) {
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

    private void copyFields(Object source, Object destination) {
        if (!source.getClass().isAssignableFrom(destination.getClass())) {
            throw new IllegalArgumentException(
                    String.format("Destination class [%s] must be same or subclass as source class [%s]",
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