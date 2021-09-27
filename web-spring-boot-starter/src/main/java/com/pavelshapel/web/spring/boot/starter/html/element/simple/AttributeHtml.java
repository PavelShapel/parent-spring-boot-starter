package com.pavelshapel.web.spring.boot.starter.html.element.simple;

import com.pavelshapel.web.spring.boot.starter.html.element.Html;
import lombok.Value;

import java.util.Set;

@Value
public class AttributeHtml implements Html {
    String key;
    Set<String> values;

    @Override
    public String toString() {
        return String.format("%s=\"%s\"", key, String.join(" ", values));
    }
}