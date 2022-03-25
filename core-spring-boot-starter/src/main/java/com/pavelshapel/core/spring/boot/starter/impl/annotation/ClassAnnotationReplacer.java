package com.pavelshapel.core.spring.boot.starter.impl.annotation;

import com.pavelshapel.core.spring.boot.starter.api.annotation.AnnotationReplacer;
import lombok.SneakyThrows;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class ClassAnnotationReplacer implements AnnotationReplacer {
    private static final String ANNOTATION_METHOD = "annotationData";
    private static final String ANNOTATIONS = "annotations";

    @SneakyThrows
    @Override
    public <T extends Annotation> void replace(Class<?> targetClass, Class<T> annotationClass, T newAnnotation) {
        Method method = Class.class.getDeclaredMethod(ANNOTATION_METHOD, null);
        method.setAccessible(true);
        Object annotationData = method.invoke(targetClass);
        Field annotations = annotationData.getClass().getDeclaredField(ANNOTATIONS);
        annotations.setAccessible(true);
        Map<Class<T>, T> map = (Map<Class<T>, T>) annotations.get(annotationData);
        map.put(annotationClass, newAnnotation);
    }
}
