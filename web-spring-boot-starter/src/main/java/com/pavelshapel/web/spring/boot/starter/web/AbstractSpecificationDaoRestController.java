package com.pavelshapel.web.spring.boot.starter.web;

import com.pavelshapel.core.spring.boot.starter.api.converter.FromDtoConverter;
import com.pavelshapel.core.spring.boot.starter.api.converter.ToDtoConverter;
import com.pavelshapel.core.spring.boot.starter.api.model.Dto;
import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.core.spring.boot.starter.api.service.DaoService;
import com.pavelshapel.core.spring.boot.starter.api.service.SpecificationDaoService;
import com.pavelshapel.core.spring.boot.starter.impl.web.search.SearchCriteria;
import com.pavelshapel.core.spring.boot.starter.impl.web.search.SearchSpecification;
import com.pavelshapel.web.spring.boot.starter.html.element.table.TableHtml;
import com.pavelshapel.web.spring.boot.starter.html.factory.Factories;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

@Getter(AccessLevel.PROTECTED)
public abstract class AbstractSpecificationDaoRestController<ID, E extends Entity<ID>, D extends Dto<ID>> extends AbstractDaoRestController<ID, E, D> {
    private final SearchSpecification<E> searchSpecification;

    protected AbstractSpecificationDaoRestController(DaoService<ID, E> daoService, FromDtoConverter<ID, D, E> fromDtoConverter, ToDtoConverter<ID, E, D> toDtoConverter, SearchSpecification<E> searchSpecification) {
        super(daoService, fromDtoConverter, toDtoConverter);
        this.searchSpecification = searchSpecification;
    }

    @Autowired
    private Factories htmlFactories;

    @GetMapping(path = SEARCH_PATH, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<D>> findAll(@Valid SearchCriteria searchCriteria, @PageableDefault Pageable pageable) {
        searchSpecification.setSearchCriteria(searchCriteria);
        Page<D> page = ((SpecificationDaoService<ID, E>) getDaoService()).findAll(searchSpecification, pageable).map(getToDtoConverter()::convert);
        return ResponseEntity.ok(page);
    }

    @GetMapping(path = TABLE_PATH, produces = TEXT_HTML_VALUE)
    public ResponseEntity<String> getTable(@Valid SearchCriteria searchCriteria, @PageableDefault Pageable pageable) {
        searchSpecification.setSearchCriteria(searchCriteria);
        Page<E> page = ((SpecificationDaoService<ID, E>) getDaoService()).findAll(searchSpecification, pageable);
        return ResponseEntity.ok(htmlFactories.getFactory(TableHtml.class)
                .create(getDaoService().getEntityClass(), page).toString());
    }
}