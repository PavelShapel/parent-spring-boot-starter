package com.pavelshapel.core.spring.boot.starter.api.bean;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public interface BeansCollection<T> {
    Map<String, T> getBeans();

    Optional<T> getBean(String beanName);

    <E extends T> Optional<T> getBean(Class<E> beanClass);

    Optional<T> getBean(Predicate<T> predicate);
}
