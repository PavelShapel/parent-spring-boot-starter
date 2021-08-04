package com.pavelshapel.web.spring.boot.starter.controller;

import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import com.pavelshapel.jpa.spring.boot.starter.repository.search.SearchCriteria;
import com.pavelshapel.jpa.spring.boot.starter.repository.search.SearchSpecification;
import com.pavelshapel.jpa.spring.boot.starter.service.jpa.JpaService;
import com.pavelshapel.stream.spring.boot.starter.util.StreamUtils;
import com.pavelshapel.web.spring.boot.starter.controller.converter.FromDtoConverter;
import com.pavelshapel.web.spring.boot.starter.controller.converter.ToDtoConverter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor
public abstract class AbstractJpaRestController<T extends AbstractEntity> {
    public static final String ID_PATH = "/{id}" + StringUtils.EMPTY;
    public static final String PAGING_PATH = "/page" + StringUtils.EMPTY;
    public static final String SEARCH_PATH = "/search" + StringUtils.EMPTY;
    public static final String PARENTAGE_PATH = "/parentage" + ID_PATH;

    private final JpaService<T> jpaService;
    private final SearchSpecification<T> searchSpecification;
    private final FromDtoConverter<T> fromDtoConverter;
    private final ToDtoConverter<T> toDtoConverter;

    @Autowired
    private StreamUtils streamUtils;

    @PostMapping
    public ResponseEntity<T> save(@RequestBody @Valid T entity) {
        T dto = fromDtoConverter
                .andThen(jpaService::save)
                .andThen(toDtoConverter)
                .convert(entity);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(ID_PATH)
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(location).body(dto);
    }

    @PutMapping(ID_PATH)
    public ResponseEntity<T> update(@PathVariable Long id, @RequestBody @Valid T entity) {
        return fromDtoConverter
                .andThen(pojo -> jpaService.update(id, pojo))
                .andThen(toDtoConverter)
                .andThen(ResponseEntity::ok)
                .convert(entity);
    }


    @GetMapping(ID_PATH)
    public ResponseEntity<T> findById(@PathVariable Long id) {
        return ((Converter<Long, T>) jpaService::findById)
                .andThen(toDtoConverter)
                .andThen(ResponseEntity::ok)
                .convert(id);
    }

    @GetMapping
    public ResponseEntity<List<T>> findAll() {
        return jpaService.findAll().stream()
                .map(toDtoConverter::convert)
                .collect(streamUtils.toResponseEntityList());
    }

    @GetMapping(PAGING_PATH)
    public ResponseEntity<Page<T>> findAll(@PageableDefault Pageable pageable) {
        Page<T> dto = jpaService.findAll(pageable).map(toDtoConverter::convert);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(SEARCH_PATH)
    public ResponseEntity<Page<T>> findAll(@Valid SearchCriteria searchCriteria, @PageableDefault Pageable pageable) {
        searchSpecification.setSearchCriteria(searchCriteria);
        Page<T> dto = jpaService.findAll(searchSpecification, pageable).map(toDtoConverter::convert);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(PARENTAGE_PATH)
    public ResponseEntity<List<T>> getParentage(@PathVariable Long id) {
        return jpaService.getParentage(id).stream()
                .map(toDtoConverter::convert)
                .collect(streamUtils.toResponseEntityList());
    }

    @DeleteMapping(ID_PATH)
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        jpaService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}