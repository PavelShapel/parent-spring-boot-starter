package com.pavelshapel.random.spring.boot.starter.randomizer;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public interface BeansCollection<T> {
    Map<String, T> getBeans();

    Optional<T> getBean(String beanName);

    Optional<T> getBean(Class<?> beanClass);

    Optional<T> getBean(Predicate<T> predicate);
}
