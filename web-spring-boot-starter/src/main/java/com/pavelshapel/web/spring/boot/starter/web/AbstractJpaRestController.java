package com.pavelshapel.web.spring.boot.starter.web;

import com.pavelshapel.aop.spring.boot.starter.log.method.Loggable;
import com.pavelshapel.core.spring.boot.starter.util.StreamUtils;
import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import com.pavelshapel.jpa.spring.boot.starter.repository.search.SearchCriteria;
import com.pavelshapel.jpa.spring.boot.starter.repository.search.SearchSpecification;
import com.pavelshapel.jpa.spring.boot.starter.service.jpa.JpaService;
import com.pavelshapel.web.spring.boot.starter.web.converter.FromDtoConverter;
import com.pavelshapel.web.spring.boot.starter.web.converter.ToDtoConverter;
import com.pavelshapel.web.spring.boot.starter.html.element.table.TableHtml;
import com.pavelshapel.web.spring.boot.starter.html.factory.impl.HtmlFactories;
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
public abstract class AbstractJpaRestController<T extends AbstractEntity> {
    public static final String ID_PATH = "/{id}" + EMPTY;
    public static final String SEARCH_PATH = "/search" + EMPTY;
    public static final String PARENTAGE_PATH = ID_PATH + "/parentage";
    public static final String FORM_PATH = ID_PATH + "/form";
    public static final String TABLE_PATH = "/table" + EMPTY;

    private final JpaService<T> jpaService;
    private final SearchSpecification<T> searchSpecification;
    private final FromDtoConverter<T> fromDtoConverter;
    private final ToDtoConverter<T> toDtoConverter;

    @Autowired
    private StreamUtils streamUtils;
    @Autowired
    private HtmlFactories htmlFactories;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
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

    @PutMapping(path = ID_PATH, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<T> update(@PathVariable Long id, @RequestBody @Valid T entity) {
        return fromDtoConverter
                .andThen(pojo -> jpaService.update(id, pojo))
                .andThen(toDtoConverter)
                .andThen(ResponseEntity::ok)
                .convert(entity);
    }


    @GetMapping(path = ID_PATH, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<T> findById(@PathVariable Long id) {
        return ((Converter<Long, T>) jpaService::findById)
                .andThen(toDtoConverter)
                .andThen(ResponseEntity::ok)
                .convert(id);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<T>> findAll() {
        return jpaService.findAll().stream()
                .map(toDtoConverter::convert)
                .collect(streamUtils.toResponseEntityList());
    }

    @GetMapping(path = SEARCH_PATH, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<T>> findAll(@Valid SearchCriteria searchCriteria, @PageableDefault Pageable pageable) {
        searchSpecification.setSearchCriteria(searchCriteria);
        Page<T> dto = jpaService.findAll(searchSpecification, pageable).map(toDtoConverter::convert);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(path = PARENTAGE_PATH, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<T>> getParentage(@RequestParam(required = false, defaultValue = "false") Boolean reverse,
                                                @PathVariable Long id) {
        List<T> dto = jpaService.getParentage(id).stream()
                .map(toDtoConverter::convert)
                .collect(streamUtils.toList(reverse));
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(path = ID_PATH, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        jpaService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = TABLE_PATH, produces = TEXT_HTML_VALUE)
    public ResponseEntity<String> getTable(@Valid SearchCriteria searchCriteria, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(htmlFactories.getFactory(TableHtml.class)
                .create(jpaService.getEntityClass(), findAll(searchCriteria, pageable).getBody())
                .toString());
    }
}