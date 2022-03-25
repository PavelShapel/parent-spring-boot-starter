package com.pavelshapel.core.spring.boot.starter.bpp;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SelfAutowired {
}
