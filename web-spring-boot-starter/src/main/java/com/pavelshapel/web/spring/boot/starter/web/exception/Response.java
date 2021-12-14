package com.pavelshapel.web.spring.boot.starter.web.exception;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@SuperBuilder
@NoArgsConstructor
public class Response {
    @Builder.Default
    private String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    private String path;
    private String status;
    private String message;
}