package com.pavelshapel.core.spring.boot.starter.impl.bean;

import com.pavelshapel.core.spring.boot.starter.api.bean.BeansCollection;
import com.pavelshapel.core.spring.boot.starter.api.util.StreamUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractBeansCollection<T> implements BeansCollection<T> {
    @Autowired
    StreamUtils streamUtils;
    @Autowired
    ObjectFactory<Map<String, T>> beans;

    @Override
    public Map<String, T> getBeans() {
        return beans.getObject();
    }

    @Override
    public Optional<T> getBean(String beanName) {
        return Optional.ofNullable(getBeans())
                .map(map -> map.get(beanName));
    }

    @Override
    public <E extends T> Optional<T> getBean(Class<E> beanClass) {
        return Optional.ofNullable(beanClass)
                .map(Class::getSimpleName)
                .map(StringUtils::uncapitalize)
                .flatMap(this::getBean);
    }

    @Override
    public Optional<T> getBean(Predicate<T> predicate) {
        return getBeans().values().stream()
                .filter(predicate)
                .collect(streamUtils.toSingleton());
    }
}