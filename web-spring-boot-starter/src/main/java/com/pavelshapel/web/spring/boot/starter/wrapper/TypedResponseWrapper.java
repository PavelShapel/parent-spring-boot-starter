package com.pavelshapel.web.spring.boot.starter.wrapper;

import lombok.Value;

@Value
public class TypedResponseWrapper {
    Object value;
    String type;
}