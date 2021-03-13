package com.pavelshapel.core.spring.boot.starter.bean;

import com.pavelshapel.stream.spring.boot.starter.util.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.function.Predicate;

public abstract class AbstractBeansCollection<T> implements BeansCollection<T> {
    @Autowired
    private StreamUtils streamUtils;

    @Override
    public Optional<T> getBean(String beanName) {
        return Optional.ofNullable(getBeans().get(beanName));
    }

    @Override
    public Optional<T> getBean(Class<?> beanClass) {
        final String beanName = StringUtils.uncapitalize(beanClass.getSimpleName());

        return getBean(beanName);
    }

    @Override
    public Optional<T> getBean(Predicate<T> predicate) {
        return getBeans().values().stream()
                .filter(predicate)
                .collect(streamUtils.toSingleton());
    }
}