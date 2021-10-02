package com.pavelshapel.jpa.spring.boot.starter.service.jpa;

import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import com.pavelshapel.jpa.spring.boot.starter.repository.AbstractJpaRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter(AccessLevel.PROTECTED)
public abstract class AbstractJpaService<T extends AbstractEntity> implements JpaService<T> {
    @Autowired
    private AbstractJpaRepository<T> abstractJpaRepository;

    private final Class<T> entityClass;

    @Override
    public T createAndSave() {
        return save(create());
    }

    @Override
    public T save(T entity) {
        return abstractJpaRepository.save(entity);
    }

    @Override
    public T update(Long id, T entity) {
        entity.setId(null);
        T entityFromDatabase = findById(id);
        copyFields(entity, entityFromDatabase);
        return abstractJpaRepository.save(entityFromDatabase);
    }

    @Override
    public List<T> saveAll(Iterable<T> entities) {
        return abstractJpaRepository.saveAll(entities);
    }


    @Override
    public T findById(Long id) {
        return abstractJpaRepository.findById(id).get();
    }

    @Override
    public List<T> findAllById(Iterable<Long> ids) {
        return abstractJpaRepository.findAllById(ids);
    }

    @Override
    public List<T> findAll() {
        return abstractJpaRepository.findAll();
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return abstractJpaRepository.findAll(pageable);
    }


    @Override
    public void deleteById(Long id) {
        abstractJpaRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        abstractJpaRepository.deleteAll();
    }


    @Override
    public boolean existsById(Long id) {
        return abstractJpaRepository.existsById(id);
    }

    @Override
    public long getCount() {
        return abstractJpaRepository.count();
    }

    @Override
    public List<T> findAll(Specification<T> specification) {
        return abstractJpaRepository.findAll(specification);
    }

    @Override
    public Page<T> findAll(Specification<T> specification, Pageable pageable) {
        return abstractJpaRepository.findAll(specification, pageable);
    }

    @Override
    public long getCount(Specification<T> specification) {
        return abstractJpaRepository.count(specification);
    }

    @Override
    public List<T> getParentage(Long id) {
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
        return entityClass;
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