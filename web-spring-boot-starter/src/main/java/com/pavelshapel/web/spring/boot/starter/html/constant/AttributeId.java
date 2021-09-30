package com.pavelshapel.web.spring.boot.starter.html.constant;


public enum AttributeId {
    ALIGN,
    BGCOLOR,
    BORDER,
    CELLPADDING,
    CHARSET,
    COLSPAN,
    WIDTH,
    TYPE;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
