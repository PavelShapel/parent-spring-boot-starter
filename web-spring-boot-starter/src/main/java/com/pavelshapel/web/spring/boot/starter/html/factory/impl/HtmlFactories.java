package com.pavelshapel.web.spring.boot.starter.html.factory.impl;

import com.pavelshapel.core.spring.boot.starter.bean.BeansCollection;
import com.pavelshapel.web.spring.boot.starter.html.element.Html;
import com.pavelshapel.web.spring.boot.starter.html.factory.Factories;
import com.pavelshapel.web.spring.boot.starter.html.factory.Factory;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;

public class HtmlFactories implements Factories {
    @Autowired
    private BeansCollection<Factory<?>> beansCollection;

    @Override
    public <T extends Html> Factory<T> getFactory(Class<T> beanClass) {
        return beansCollection.getBean(factory -> factory.getBeanClass().equals(beanClass))
                .map(factory -> (Factory<T>) factory)
                .orElseThrow(NotImplementedException::new);
    }
}
