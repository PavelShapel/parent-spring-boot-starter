package com.pavelshapel.web.spring.boot.starter.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypedResponseWrapper {
    Object value;
    String type;
}