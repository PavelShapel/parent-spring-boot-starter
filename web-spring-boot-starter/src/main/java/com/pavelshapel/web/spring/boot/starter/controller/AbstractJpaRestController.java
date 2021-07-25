package com.pavelshapel.web.spring.boot.starter.controller;

import com.pavelshapel.jpa.spring.boot.starter.entity.AbstractEntity;
import com.pavelshapel.jpa.spring.boot.starter.service.jpa.JpaService;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

public abstract class AbstractJpaRestController<T extends AbstractEntity> {
    private static final String ID_PATH = "/{id:[\\d]+}";
    private static final String PAGING_PATH = "/page";

    @Getter(AccessLevel.PROTECTED)
    private final JpaService<T> jpaService;

    protected AbstractJpaRestController(JpaService<T> jpaService) {
        this.jpaService = jpaService;
    }

    @PostMapping
    public ResponseEntity<T> post(@RequestBody @Valid T entity) {
        T responseEntity = jpaService.save(entity);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(ID_PATH)
                .buildAndExpand(responseEntity.getId()).toUri();
        return ResponseEntity.created(location).body(responseEntity);
    }

    @PutMapping(ID_PATH)
    public ResponseEntity<T> put(@PathVariable Long id, @RequestBody @Valid T entity) {
        return jpaService.existsById(id)
                ? ResponseEntity.ok(jpaService.update(id, entity))
                : ResponseEntity.notFound().build();
    }


    @GetMapping(ID_PATH)
    public ResponseEntity<T> get(@PathVariable Long id) {
        return ResponseEntity.ok(jpaService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<T>> get() {
        return ResponseEntity.ok(jpaService.findAll());
    }

    @GetMapping(PAGING_PATH)
    public ResponseEntity<Page<T>> get(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(jpaService.findAll(pageable));
    }

    @DeleteMapping(ID_PATH)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        jpaService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}