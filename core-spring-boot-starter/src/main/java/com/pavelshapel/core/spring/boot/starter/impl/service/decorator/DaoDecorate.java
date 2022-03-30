package com.pavelshapel.core.spring.boot.starter.impl.service.decorator;


import com.pavelshapel.core.spring.boot.starter.api.service.DaoService;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface DaoDecorate {
    Class<? extends DaoService<?, ?>>[] decorations();
}