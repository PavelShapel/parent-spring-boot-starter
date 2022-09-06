package com.pavelshapel.aws.spring.boot.starter.api.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import org.springframework.http.HttpMethod;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

public interface RequestHandler {
    default boolean isPostMethod(APIGatewayProxyRequestEvent request) {
        return isRequestMethod(request, POST);
    }

    default boolean isGetMethod(APIGatewayProxyRequestEvent request) {
        return isRequestMethod(request, GET);
    }

    boolean isRequestMethod(APIGatewayProxyRequestEvent request, HttpMethod httpMethod);
}
