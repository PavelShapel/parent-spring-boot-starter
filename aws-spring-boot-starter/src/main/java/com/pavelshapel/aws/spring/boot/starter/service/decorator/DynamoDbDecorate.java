package com.pavelshapel.aws.spring.boot.starter.service.decorator;


import com.pavelshapel.aws.spring.boot.starter.service.DynamoDbService;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface DynamoDbDecorate {
    Class<? extends DynamoDbService<?, ?>>[] decorations();
}