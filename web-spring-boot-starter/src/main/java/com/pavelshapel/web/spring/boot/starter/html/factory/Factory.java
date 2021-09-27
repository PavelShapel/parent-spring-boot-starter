package com.pavelshapel.web.spring.boot.starter.html.factory;

import com.pavelshapel.web.spring.boot.starter.html.element.Html;

public interface Factory<T extends Html> {
    Class<T> getBeanClass();

    T create(Object... args);
}
