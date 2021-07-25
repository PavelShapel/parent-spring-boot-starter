package com.pavelshapel.jpa.spring.boot.starter.service.jpa.decorator;

import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ThrowableDecoratorJpaService extends AbstractDecoratorJpaService<AbstractEntity> {

    @Override
    public AbstractEntity findById(Long id) {
        verifyId(id);
        return super.findById(id);
    }

    @Override
    public List<AbstractEntity> findAllById(Iterable<Long> ids) {
        List<AbstractEntity> entities = super.findAllById(ids);
        verifyAllId(entities, ids);
        return entities;
    }

    @Override
    public List<AbstractEntity> findAll() {
        List<AbstractEntity> entities = super.findAll();
        verifyAllId(entities);
        return entities;
    }

    @Override
    public Page<AbstractEntity> findAll(Pageable pageable) {
        Page<AbstractEntity> entities = super.findAll(pageable);
        verifyCount(entities.getTotalElements());
        return entities;
    }


    @Override
    public void deleteById(Long id) {
        verifyId(id);
        super.deleteById(id);
    }

    @Override
    public void deleteAll() {
        verifyCount(super.getCount());
        super.deleteAll();
    }

    private void verifyId(Long id) {
        if (!super.existsById(id)) {
            throw createEntityNotFoundException(Collections.singletonList(id));
        }
    }

    private void verifyAllId(List<AbstractEntity> entities, Iterable<Long> ids) {
        if (entities.isEmpty()) {
            throw createEntityNotFoundException(ids);
        }
    }

    private void verifyAllId(List<AbstractEntity> entities) {
        verifyAllId(entities, Collections.emptyList());
    }

    private void verifyCount(Long count) {
        if (count == 0) {
            throw createEntityNotFoundException(Collections.emptyList());
        }
    }

    private RuntimeException createEntityNotFoundException(Iterable<Long> ids) {
        String stringOfIds = StreamSupport.stream(ids.spliterator(), false)
                .map(String::valueOf)
                .collect(Collectors.joining(", "));

        return new EntityNotFoundException(
                String.format(
                        "service: [%s]; ids: [%s]",
                        getClass().getSimpleName(),
                        stringOfIds.isEmpty() ? "empty list" : stringOfIds
                )
        );
    }
}
