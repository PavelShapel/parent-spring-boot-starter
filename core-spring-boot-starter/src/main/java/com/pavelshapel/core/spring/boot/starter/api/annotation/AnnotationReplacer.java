package com.pavelshapel.core.spring.boot.starter.api.annotation;

import java.lang.annotation.Annotation;

public interface AnnotationReplacer {
    <T extends Annotation> void replace(Class<?> targetClass, T annotation);
}
