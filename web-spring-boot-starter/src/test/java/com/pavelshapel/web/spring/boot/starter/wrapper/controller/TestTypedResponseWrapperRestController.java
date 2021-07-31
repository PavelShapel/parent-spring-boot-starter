package com.pavelshapel.web.spring.boot.starter.wrapper.controller;

import com.pavelshapel.web.spring.boot.starter.wrapper.provider.TypesProvider;
import com.pavelshapel.web.spring.boot.starter.wrapper.TypedResponseWrapperRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@TypedResponseWrapperRestController
@RequestMapping("/")
public class TestTypedResponseWrapperRestController {
    @GetMapping("/{testTypes}")
    public ResponseEntity<Object> test(@PathVariable String testTypes) {
        return ResponseEntity.ok(TypesProvider.valueOf(testTypes).getValue());
    }
}
