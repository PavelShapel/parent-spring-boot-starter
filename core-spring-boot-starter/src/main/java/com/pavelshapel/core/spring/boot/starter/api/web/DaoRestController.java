package com.pavelshapel.core.spring.boot.starter.api.web;

import com.pavelshapel.core.spring.boot.starter.api.model.Dto;
import com.pavelshapel.core.spring.boot.starter.api.model.Entity;
import com.pavelshapel.core.spring.boot.starter.impl.web.search.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public interface DaoRestController<ID, E extends Entity<ID>, D extends Dto<ID>> {
    String ID_PATH = "/{id}" + EMPTY;
    String SEARCH_PATH = "/search" + EMPTY;
    String PARENTAGE_PATH = ID_PATH + "/parentage";
    String FORM_PATH = ID_PATH + "/form";
    String TABLE_PATH = "/table" + EMPTY;

    ResponseEntity<D> save(D dto);

    ResponseEntity<D> update(ID id, D dto);

    ResponseEntity<D> findById(ID id);

    ResponseEntity<List<D>> findAll();

    ResponseEntity<Page<D>> findAll(SearchCriteria searchCriteria, Pageable pageable);

    ResponseEntity<List<D>> getParentage(Boolean reverse, ID id);

    ResponseEntity<Void> deleteById(ID id);

    ResponseEntity<Void> deleteAll();

    ResponseEntity<String> getTable(SearchCriteria searchCriteria, Pageable pageable);
}