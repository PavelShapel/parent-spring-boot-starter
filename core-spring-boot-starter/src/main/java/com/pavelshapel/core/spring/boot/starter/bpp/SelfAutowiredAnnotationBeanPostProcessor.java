package com.pavelshapel.core.spring.boot.starter.bpp;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings("NullableProblems")
public class SelfAutowiredAnnotationBeanPostProcessor implements BeanPostProcessor, Ordered {
    private final Map<String, Object> selfAutowiredBeans = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        getFilteredSelfAutowiredFieldsStream(bean)
                .flatMap(Stream::findFirst)
                .ifPresent(unused -> selfAutowiredBeans.put(beanName, bean));
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Object originalBean = selfAutowiredBeans.get(beanName);
        getFilteredSelfAutowiredFieldsStream(originalBean)
                .ifPresent(fieldStream -> fieldStream.forEach(field -> setField(field, originalBean, bean)));
        return bean;
    }

    private void setField(Field field, Object target, Object value) {
        field.setAccessible(true);
        ReflectionUtils.setField(field, target, value);
    }

    private Optional<Stream<Field>> getFilteredSelfAutowiredFieldsStream(Object bean) {
        return Optional.ofNullable(bean)
                .map(Object::getClass)
                .map(Class::getDeclaredFields)
                .map(Arrays::stream)
                .map(this::filterSelfAutowiredFields);
    }

    private Stream<Field> filterSelfAutowiredFields(Stream<Field> fieldStream) {
        return fieldStream.filter(field -> field.isAnnotationPresent(SelfAutowired.class));
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}