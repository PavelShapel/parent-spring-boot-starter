package com.pavelshapel.web.spring.boot.starter.controller.exception;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class ErrorResponse extends Response {
    @NonNull
    @Singular("error")
    private List<String> error;
}