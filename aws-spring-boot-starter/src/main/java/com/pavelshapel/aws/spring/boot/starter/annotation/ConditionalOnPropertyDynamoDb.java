package com.pavelshapel.aws.spring.boot.starter.annotation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.pavelshapel.aws.spring.boot.starter.annotation.ConditionalOnPropertyDynamoDb.DYNAMO_DB;
import static com.pavelshapel.aws.spring.boot.starter.properties.AwsProperties.PREFIX;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Inherited
@ConditionalOnProperty(prefix = PREFIX, name = DYNAMO_DB + ".enabled", havingValue = "true")
public @interface ConditionalOnPropertyDynamoDb {
    String DYNAMO_DB = "dynamodb";
}
