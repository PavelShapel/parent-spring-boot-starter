package com.pavelshapel.jpa.spring.boot.starter.service.decorator;


import com.pavelshapel.jpa.spring.boot.starter.service.JpaService;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface JpaDecorate {
    Class<? extends JpaService<?, ?>>[] decorations();
}