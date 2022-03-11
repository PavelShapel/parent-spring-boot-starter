package com.pavelshapel.web.spring.boot.starter.web;

import com.pavelshapel.aop.spring.boot.starter.log.method.Loggable;
import com.pavelshapel.core.spring.boot.starter.model.Dto;
import com.pavelshapel.core.spring.boot.starter.model.Entity;
import com.pavelshapel.core.spring.boot.starter.util.StreamUtils;
import com.pavelshapel.jpa.spring.boot.starter.repository.search.SearchCriteria;
import com.pavelshapel.jpa.spring.boot.starter.repository.search.SearchSpecification;
import com.pavelshapel.jpa.spring.boot.starter.service.jpa.JpaService;
import com.pavelshapel.web.spring.boot.starter.html.element.table.TableHtml;
import com.pavelshapel.web.spring.boot.starter.html.factory.Factories;
import com.pavelshapel.web.spring.boot.starter.web.converter.FromDtoConverter;
import com.pavelshapel.web.spring.boot.starter.web.converter.ToDtoConverter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter(AccessLevel.PROTECTED)
@Loggable
public abstract class AbstractJpaRestController<ID, E extends Entity<ID>, D extends Dto<ID>> {
    public static final String ID_PATH = "/{id}" + EMPTY;
    public static final String SEARCH_PATH = "/search" + EMPTY;
    public static final String PARENTAGE_PATH = ID_PATH + "/parentage";
    public static final String FORM_PATH = ID_PATH + "/form";
    public static final String TABLE_PATH = "/table" + EMPTY;

    private final JpaService<ID, E> jpaService;
    private final SearchSpecification<E> searchSpecification;
    private final FromDtoConverter<ID, D, E> fromDtoConverter;
    private final ToDtoConverter<ID, E, D> toDtoConverter;

    @Autowired
    private StreamUtils streamUtils;
    @Autowired
    private Factories htmlFactories;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<D> save(@RequestBody @Valid D dto) {
        dto = fromDtoConverter
                .andThen(jpaService::save)
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
                .andThen(entity -> jpaService.update(id, entity))
                .andThen(toDtoConverter)
                .andThen(ResponseEntity::ok)
                .convert(dto);
    }


    @GetMapping(path = ID_PATH, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<D> findById(@PathVariable ID id) {
        return ((Converter<ID, E>) jpaService::findById)
                .andThen(toDtoConverter)
                .andThen(ResponseEntity::ok)
                .convert(id);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<D>> findAll() {
        return jpaService.findAll().stream()
                .map(toDtoConverter::convert)
                .collect(streamUtils.toResponseEntityList());
    }

    @GetMapping(path = SEARCH_PATH, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<D>> findAll(@Valid SearchCriteria searchCriteria, @PageableDefault Pageable pageable) {
        searchSpecification.setSearchCriteria(searchCriteria);
        Page<D> page = jpaService.findAll(searchSpecification, pageable).map(toDtoConverter::convert);
        return ResponseEntity.ok(page);
    }

    @GetMapping(path = PARENTAGE_PATH, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<D>> getParentage(@RequestParam(required = false, defaultValue = "false") Boolean reverse,
                                                @PathVariable ID id) {
        return jpaService.getParentage(id).stream()
                .map(toDtoConverter::convert)
                .collect(streamUtils.toResponseEntityList(reverse));
    }

    @DeleteMapping(path = ID_PATH, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteById(@PathVariable ID id) {
        jpaService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = TABLE_PATH, produces = TEXT_HTML_VALUE)
    public ResponseEntity<String> getTable(@Valid SearchCriteria searchCriteria, @PageableDefault Pageable pageable) {
        searchSpecification.setSearchCriteria(searchCriteria);
        Page<E> page = jpaService.findAll(searchSpecification, pageable);
        return ResponseEntity.ok(htmlFactories.getFactory(TableHtml.class)
                .create(jpaService.getEntityClass(), page).toString());
    }
}