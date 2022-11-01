package com.pavelshapel.aop.spring.boot.starter.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MethodParameter {
    int index;
    Object value;
}
