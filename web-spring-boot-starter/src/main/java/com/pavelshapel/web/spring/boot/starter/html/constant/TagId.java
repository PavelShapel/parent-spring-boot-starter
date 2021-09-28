package com.pavelshapel.web.spring.boot.starter.html.constant;

public enum TagId {
    BODY,
    TITLE,
    META,
    HEAD,
    HTML,

    H1,

    TABLE,
    THEAD,
    TBODY,
    TFOOT,
    TR,
    TH,
    TD,

    BUTTON;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
