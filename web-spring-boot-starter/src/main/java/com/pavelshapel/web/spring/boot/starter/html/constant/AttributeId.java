package com.pavelshapel.web.spring.boot.starter.html.constant;


import com.pavelshapel.web.spring.boot.starter.html.element.Html;

public enum AttributeId implements Html {
    ALIGN,
    BGCOLOR,
    BORDER,
    CELLPADDING,
    CHARSET,
    COLSPAN,
    ONCLICK,
    WIDTH,
    TYPE;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
