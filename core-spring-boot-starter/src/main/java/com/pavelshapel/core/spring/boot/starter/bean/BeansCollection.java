package com.pavelshapel.core.spring.boot.starter.bean;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public interface BeansCollection<T> {
    Map<String, T> getBeans();

    Optional<T> getBean(String beanName);

    Optional<T> getBean(Class<?> beanClass);

    Optional<T> getBean(Predicate<T> predicate);
}
