//package com.pavelshapel.aws.spring.boot.starter.service.decorator;
//
//import com.pavelshapel.core.spring.boot.starter.model.Entity;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.domain.Specification;
//
//import javax.persistence.EntityNotFoundException;
//import java.util.List;
//import java.util.stream.StreamSupport;
//
//import static java.util.Collections.emptyList;
//import static java.util.Collections.singletonList;
//import static java.util.Objects.isNull;
//import static java.util.stream.Collectors.joining;
//
//public abstract class ThrowableDecoratorDynamoDbService<ID, T extends Entity<ID>> extends AbstractDecoratorDynamoDbService<ID, T> {
//
//    @Override
//    public T update(ID id, T entity) {
//        verifyId(id);
//        return super.update(id, entity);
//    }
//
//    @Override
//    public T findById(ID id) {
//        verifyId(id);
//        return super.findById(id);
//    }
//
//    @Override
//    public List<T> findAllById(Iterable<ID> ids) {
//        List<T> entities = super.findAllById(ids);
//        verifyCollection(entities, ids);
//        return entities;
//    }
//
//    @Override
//    public List<T> findAll() {
//        List<T> entities = super.findAll();
//        verifyCollection(entities);
//        return entities;
//    }
//
//    @Override
//    public Page<T> findAll(Pageable pageable) {
//        Page<T> entities = super.findAll(pageable);
//        verifyCount(entities.getTotalElements());
//        return entities;
//    }
//
//
//    @Override
//    public void deleteById(ID id) {
//        verifyId(id);
//        super.deleteById(id);
//    }
//
//    @Override
//    public void deleteAll() {
//        verifyCount(super.getCount());
//        super.deleteAll();
//    }
//
//    @Override
//    public List<T> getParentage(ID id) {
//        List<T> entities = super.getParentage(id);
//        verifyCollection(entities);
//        return entities;
//    }
//
//    protected void verifyId(ID id) {
//        if (isNull(id) || !super.existsById(id)) {
//            throw createEntityNotFoundException(singletonList(id));
//        }
//    }
//
//    protected void verifyCollection(List<T> entities, Iterable<ID> ids) {
//        if (entities.isEmpty()) {
//            throw createEntityNotFoundException(ids);
//        }
//    }
//
//    protected void verifyCollection(List<T> entities) {
//        verifyCollection(entities, emptyList());
//    }
//
//    protected void verifyCount(long count) {
//        if (count == 0) {
//            throw createEntityNotFoundException(emptyList());
//        }
//    }
//
//    protected RuntimeException createEntityNotFoundException(Iterable<ID> ids) {
//        String stringOfIds = StreamSupport.stream(ids.spliterator(), false)
//                .map(String::valueOf)
//                .collect(joining(", "));
//
//        return new EntityNotFoundException(
//                String.format(
//                        "service: [%s]; ids: [%s]",
//                        getClass().getSimpleName(),
//                        stringOfIds.isEmpty() ? "not defined" : stringOfIds
//                )
//        );
//    }
//}