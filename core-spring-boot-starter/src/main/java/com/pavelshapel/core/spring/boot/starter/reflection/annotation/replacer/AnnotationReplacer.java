package com.pavelshapel.core.spring.boot.starter.reflection.annotation.replacer;

import java.lang.annotation.Annotation;

public interface AnnotationReplacer {
    <T extends Annotation> void replace(Class<?> targetClass, Class<T> annotationClass, T newAnnotation);
}
