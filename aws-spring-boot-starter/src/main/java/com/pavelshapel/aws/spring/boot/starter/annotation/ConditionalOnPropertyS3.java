package com.pavelshapel.aws.spring.boot.starter.annotation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.pavelshapel.aws.spring.boot.starter.annotation.ConditionalOnPropertyS3.S3;
import static com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties.PREFIX;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Inherited
@ConditionalOnProperty(prefix = PREFIX, name = S3 + ".enabled", havingValue = "true")
public @interface ConditionalOnPropertyS3 {
    String S3 = "s3";
}
