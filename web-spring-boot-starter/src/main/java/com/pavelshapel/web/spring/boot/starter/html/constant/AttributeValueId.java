package com.pavelshapel.web.spring.boot.starter.html.constant;

import lombok.Getter;

import java.nio.charset.StandardCharsets;

public enum AttributeValueId {
    //ALIGN
    LEFT("left"),
    CENTER("center"),
    RIGHT("right"),

    //BUTTON
    SIMPLE_BUTTON("button"),
    SUBMIT_BUTTON("submit"),
    RESET_BUTTON("reset"),

    //CRUD
    CRUD_INSERT("INS"),
    CRUD_UPDATE("UPD"),
    CRUD_DELETE("DEL"),

    //COLOR
    GRAY("gray"),

    //CHARSET
    UTF_8(StandardCharsets.UTF_8.name()),

    //NUMBER
    INT_1("1"),
    INT_5("5"),
    INT_10("10"),
    INT_25("25"),

    //WIDTH
    WIDTH_10_PERCENT("10%"),
    WIDTH_80_PERCENT("80%");

    @Getter
    private final String value;

    AttributeValueId(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}