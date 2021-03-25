package com.pavelshapel.web.spring.boot.starter.wrapper.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TestRestController {
    @GetMapping("/{testTypes}")
    public ResponseEntity<Object> test(@PathVariable String testTypes) {
        return ResponseEntity.ok(TestTypes.valueOf(testTypes).getValue());
    }
}
