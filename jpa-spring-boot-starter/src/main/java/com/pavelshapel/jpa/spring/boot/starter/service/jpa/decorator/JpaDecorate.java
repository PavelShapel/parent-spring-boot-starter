package com.pavelshapel.jpa.spring.boot.starter.service.jpa.decorator;


import com.pavelshapel.jpa.spring.boot.starter.service.jpa.JpaService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JpaDecorate {
    Class<? extends JpaService<?>>[] decorations();
}