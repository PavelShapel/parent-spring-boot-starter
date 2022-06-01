package com.pavelshapel.jpa.spring.boot.starter.service.decorator;


import com.pavelshapel.jpa.spring.boot.starter.service.DaoService;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface DecorateDaoService {
    Class<? extends DaoService<?, ?>>[] decorations();
}