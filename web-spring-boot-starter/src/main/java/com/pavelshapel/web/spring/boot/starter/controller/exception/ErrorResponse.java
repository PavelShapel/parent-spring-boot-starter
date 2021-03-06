package com.pavelshapel.web.spring.boot.starter.controller.exception;

import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class ErrorResponse extends Response {
    @NonNull
    private String error;
}