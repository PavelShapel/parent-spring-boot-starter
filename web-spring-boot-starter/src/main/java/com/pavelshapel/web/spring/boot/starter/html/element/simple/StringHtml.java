package com.pavelshapel.web.spring.boot.starter.html.element.simple;

import com.pavelshapel.web.spring.boot.starter.html.element.Html;
import lombok.Value;

@Value
public class StringHtml implements Html {
    String value;

    @Override
    public String toString() {
        return value;
    }
}