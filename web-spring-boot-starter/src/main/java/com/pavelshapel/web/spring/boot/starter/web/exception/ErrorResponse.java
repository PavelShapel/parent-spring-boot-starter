package com.pavelshapel.web.spring.boot.starter.web.exception;

import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class ErrorResponse extends Response {
    private String error;
}