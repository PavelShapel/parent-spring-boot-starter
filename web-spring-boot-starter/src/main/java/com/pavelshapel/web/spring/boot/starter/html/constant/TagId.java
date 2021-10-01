package com.pavelshapel.web.spring.boot.starter.html.constant;

import com.pavelshapel.web.spring.boot.starter.html.element.Html;

public enum TagId implements Html {
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
