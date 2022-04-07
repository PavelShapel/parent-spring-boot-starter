package com.pavelshapel.web.spring.boot.starter.web;

import com.pavelshapel.aop.spring.boot.starter.log.method.Loggable;
import com.pavelshapel.core.spring.boot.starter.api.converter.FromDtoConverter;
import com.pavelshapel.core.spring.boot.starter.api.converter.ToDtoConverter;
import com.pavelshapel.core.spring.boot.starter.api.model.Dto;
import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.core.spring.boot.starter.api.service.DaoService;
import com.pavelshapel.core.spring.boot.starter.api.util.StreamUtils;
import com.pavelshapel.core.spring.boot.starter.impl.web.search.SearchCriteria;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter(AccessLevel.PROTECTED)
@Loggable
public abstract class AbstractDaoRestController<ID, E extends Entity<ID>, D extends Dto<ID>> {
    public static final String ID_PATH = "/{id}" + EMPTY;
    public static final String SEARCH_PATH = "/search" + EMPTY;
    public static final String PARENTAGE_PATH = ID_PATH + "/parentage";
    public static final String FORM_PATH = ID_PATH + "/form";
    public static final String TABLE_PATH = "/table" + EMPTY;

    private final DaoService<ID, E> daoService;
    private final FromDtoConverter<ID, D, E> fromDtoConverter;
    private final ToDtoConverter<ID, E, D> toDtoConverter;

    @Autowired
    private StreamUtils streamUtils;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<D> save(@RequestBody @Valid D dto) {
        dto = fromDtoConverter
                .andThen(daoService::save)
                .andThen(toDtoConverter)
                .convert(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(ID_PATH)
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(location).body(dto);
    }

    @PutMapping(path = ID_PATH, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<D> update(@PathVariable ID id, @RequestBody @Valid D dto) {
        return fromDtoConverter
                .andThen(entity -> daoService.update(id, entity))
                .andThen(toDtoConverter)
                .andThen(ResponseEntity::ok)
                .convert(dto);
    }


    @GetMapping(path = ID_PATH, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<D> findById(@PathVariable ID id) {
        return ((Converter<ID, E>) daoService::findById)
                .andThen(toDtoConverter)
                .andThen(ResponseEntity::ok)
                .convert(id);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<D>> findAll(@Valid SearchCriteria searchCriteria) {
        return Optional.ofNullable(searchCriteria)
                .filter(criteria -> Objects.nonNull(criteria.getField()))
                .filter(criteria -> Objects.nonNull(criteria.getValue()))
                .filter(criteria -> Objects.nonNull(criteria.getOperation()))
                .map(criteria -> daoService.findAll(searchCriteria))
                .orElseGet(daoService::findAll)
                .stream()
                .map(toDtoConverter::convert)
                .collect(streamUtils.toResponseEntityList());
    }

    @GetMapping(path = PARENTAGE_PATH, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<D>> getParentage(@RequestParam(required = false, defaultValue = "false") Boolean reverse,
                                                @PathVariable ID id) {
        return daoService.getParentage(id).stream()
                .map(toDtoConverter::convert)
                .collect(streamUtils.toResponseEntityList(reverse));
    }

    @DeleteMapping(path = ID_PATH, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteById(@PathVariable ID id) {
        daoService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteAll() {
        daoService.deleteAll();
        return ResponseEntity.ok().build();
    }
}