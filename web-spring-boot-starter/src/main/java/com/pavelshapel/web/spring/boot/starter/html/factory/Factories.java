package com.pavelshapel.web.spring.boot.starter.html.factory;

import com.pavelshapel.web.spring.boot.starter.html.element.Html;

public interface Factories {
    <T extends Html> Factory<T> getFactory(Class<T> beanClass);
}
