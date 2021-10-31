package com.pavelshapel.kafka.spring.boot.starter.aop;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface KafkaSender {
    String topic();
}