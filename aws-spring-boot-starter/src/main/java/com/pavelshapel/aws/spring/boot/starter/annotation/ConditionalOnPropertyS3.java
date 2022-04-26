package com.pavelshapel.aws.spring.boot.starter.annotation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.*;

import static com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties.PREFIX;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Inherited
@ConditionalOnProperty(prefix = PREFIX, name = "s3.enable", havingValue = "true")
public @interface ConditionalOnPropertyS3 {
}
