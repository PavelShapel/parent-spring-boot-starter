package com.pavelshapel.web.spring.boot.starter.controller;

import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import com.pavelshapel.jpa.spring.boot.starter.repository.search.SearchCriteria;
import com.pavelshapel.jpa.spring.boot.starter.repository.search.SearchSpecification;
import com.pavelshapel.jpa.spring.boot.starter.service.jpa.AbstractJpaService;
import com.pavelshapel.web.spring.boot.starter.controller.converter.FromDtoConverter;
import com.pavelshapel.web.spring.boot.starter.controller.converter.ToDtoConverter;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public abstract class AbstractJpaRestController<T extends AbstractEntity> {
    public static final String ID_PATH = "/{id}" + StringUtils.EMPTY;
    public static final String PAGING_PATH = "/page" + StringUtils.EMPTY;
    public static final String SEARCH_PATH = "/search" + StringUtils.EMPTY;
    public static final String PARENTAGE_PATH = "/parentage" + ID_PATH;

    @Autowired
    private AbstractJpaService<T> abstractJpaService;
    @Autowired
    private SearchSpecification<T> searchSpecification;
    @Autowired
    private FromDtoConverter<T> fromDtoConverter;
    @Autowired
    private ToDtoConverter<T> toDtoConverter;

    @PostMapping
    public ResponseEntity<T> save(@RequestBody @Valid T entity) {
        T dto = fromDtoConverter
                .andThen(location -> abstractJpaService.save(location))
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
        T dto = fromDtoConverter
                .andThen(location -> abstractJpaService.update(id, location))
                .andThen(toDtoConverter)
                .convert(entity);
        return ResponseEntity.ok(dto);
    }


    @GetMapping(ID_PATH)
    public ResponseEntity<T> findById(@PathVariable Long id) {
        return ResponseEntity.ok(abstractJpaService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<T>> findAll() {
        return ResponseEntity.ok(abstractJpaService.findAll());
    }

    @GetMapping(PAGING_PATH)
    public ResponseEntity<Page<T>> findAll(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(abstractJpaService.findAll(pageable));
    }

    @GetMapping(SEARCH_PATH)
    public ResponseEntity<Page<T>> findAll(@Valid SearchCriteria searchCriteria, @PageableDefault Pageable pageable) {
        searchSpecification.setSearchCriteria(searchCriteria);
        return ResponseEntity.ok(abstractJpaService.findAll(searchSpecification, pageable));
    }

    @GetMapping(PARENTAGE_PATH)
    public ResponseEntity<List<T>> getParentage(@PathVariable Long id) {
        return ResponseEntity.ok(abstractJpaService.getParentage(id));
    }

    @DeleteMapping(ID_PATH)
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        abstractJpaService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}