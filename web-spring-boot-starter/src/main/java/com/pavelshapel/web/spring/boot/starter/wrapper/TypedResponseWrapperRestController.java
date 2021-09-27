package com.pavelshapel.web.spring.boot.starter.wrapper;

import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

@RestController
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface TypedResponseWrapperRestController {
}