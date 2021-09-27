package com.pavelshapel.web.spring.boot.starter.html.factory;

import com.pavelshapel.web.spring.boot.starter.html.element.Html;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public abstract class AbstractFactory<T extends Html> implements Factory<T> {
    @Autowired
    private ApplicationContext applicationContext;

    private final Class<T> beanClass;

    protected AbstractFactory(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public Class<T> getBeanClass() {
        return beanClass;
    }

    @Override
    public T create(Object... args) {
        return applicationContext.getBean(beanClass, args);
    }
}
