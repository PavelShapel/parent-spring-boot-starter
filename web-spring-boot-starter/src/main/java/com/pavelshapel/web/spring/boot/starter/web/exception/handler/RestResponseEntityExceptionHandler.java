package com.pavelshapel.web.spring.boot.starter.web.exception.handler;

import com.pavelshapel.web.spring.boot.starter.web.exception.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(Exception exception, WebRequest request) {
        return createErrorResponseEntity(exception, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @Override
    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleExceptionInternal(Exception exception, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return createErrorResponseEntity(exception, headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<Object> createErrorResponseEntity(Exception exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(status.toString())
                .error(exception.getClass().toString())
                .message(exception.getMessage())
                .path(getRequestPath(request))
                .build();
        return new ResponseEntity<>(errorResponse, headers, status);
    }

    private String getRequestPath(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI();
    }
}