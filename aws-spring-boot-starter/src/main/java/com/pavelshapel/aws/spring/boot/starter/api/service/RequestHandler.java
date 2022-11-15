package com.pavelshapel.aws.spring.boot.starter.api.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import org.springframework.http.HttpMethod;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

public interface RequestHandler {
    default boolean isPostMethod(APIGatewayV2HTTPEvent request) {
        return isRequestMethod(request, POST);
    }

    default boolean isGetMethod(APIGatewayV2HTTPEvent request) {
        return isRequestMethod(request, GET);
    }

    boolean isRequestMethod(APIGatewayV2HTTPEvent request, HttpMethod httpMethod);

    String getQueryParameter(APIGatewayV2HTTPEvent request, String pathParameter);
}
