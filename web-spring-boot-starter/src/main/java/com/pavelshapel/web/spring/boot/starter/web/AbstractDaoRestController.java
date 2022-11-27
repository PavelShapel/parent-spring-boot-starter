package com.pavelshapel.web.spring.boot.starter.web;

import com.pavelshapel.aop.spring.boot.starter.annotation.Loggable;
import com.pavelshapel.core.spring.boot.starter.api.converter.FromDtoConverter;
import com.pavelshapel.core.spring.boot.starter.api.converter.ToDtoConverter;
import com.pavelshapel.core.spring.boot.starter.api.model.Dto;
import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.core.spring.boot.starter.api.util.StreamUtils;
import com.pavelshapel.jpa.spring.boot.starter.service.DaoService;
import com.pavelshapel.jpa.spring.boot.starter.service.search.SearchCriterion;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public static final String BULK_PATH = "/bulk" + EMPTY;

    private final DaoService<ID, E> daoService;
    private final FromDtoConverter<ID, D, E> fromDtoConverter;
    private final ToDtoConverter<ID, E, D> toDtoConverter;

    @Autowired
    private StreamUtils streamUtils;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<D> save(@RequestBody @Valid D dto) {
        D responseDto = fromDtoConverter
                .andThen(daoService::save)
                .andThen(toDtoConverter)
                .convert(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(ID_PATH)
                .buildAndExpand(Optional.ofNullable(responseDto).map(Dto::getId).orElse(null)).toUri();
        return ResponseEntity.created(location).body(responseDto);
    }

    @PostMapping(path = BULK_PATH, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<D>> saveAll(@RequestBody List<@Valid D> dtos) {
        List<D> responseDtos = ((Converter<List<D>, List<E>>) this::fromDtoListConverter)
                .andThen(daoService::saveAll)
                .andThen(this::toDtoListConverter)
                .convert(dtos);
        List<ID> responseIds = getIds(responseDtos);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(ID_PATH)
                .buildAndExpand(responseIds).toUri();
        return ResponseEntity.created(location).body(responseDtos);
    }

    private List<ID> getIds(List<D> responseDtos) {
        return Optional.ofNullable(responseDtos)
                .orElseGet(Collections::emptyList).stream()
                .map(Dto::getId)
                .collect(Collectors.toList());
    }

    private List<E> fromDtoListConverter(List<D> dtos) {
        return dtos.stream()
                .map(fromDtoConverter::convert)
                .collect(Collectors.toList());
    }

    private List<D> toDtoListConverter(List<E> entities) {
        return entities.stream()
                .map(toDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @PutMapping(path = ID_PATH, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<D> update(@PathVariable ID id,
                                    @RequestBody @Valid D dto) {
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
    public ResponseEntity<List<D>> findAll(@RequestBody List<@Valid SearchCriterion> searchCriteria) {
        return Optional.ofNullable(searchCriteria)
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