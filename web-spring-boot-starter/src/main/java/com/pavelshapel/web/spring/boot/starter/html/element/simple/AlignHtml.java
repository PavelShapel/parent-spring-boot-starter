package com.pavelshapel.web.spring.boot.starter.html.element.simple;

import com.pavelshapel.web.spring.boot.starter.html.element.Html;

public enum AlignHtml implements Html {
    LEFT,
    CENTER,
    RIGHT;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}