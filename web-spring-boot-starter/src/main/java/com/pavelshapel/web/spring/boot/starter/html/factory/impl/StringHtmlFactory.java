package com.pavelshapel.web.spring.boot.starter.html.factory.impl;

import com.pavelshapel.web.spring.boot.starter.html.element.simple.StringHtml;
import com.pavelshapel.web.spring.boot.starter.html.factory.AbstractFactory;

public class StringHtmlFactory extends AbstractFactory<StringHtml> {
    public StringHtmlFactory() {
        super(StringHtml.class);
    }
}
