package com.pavelshapel.aws.spring.boot.starter.api.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.http.HttpMethod;

import java.io.InputStream;

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

    InputStream requestBodyToInputStream(APIGatewayV2HTTPEvent request);

    ObjectMetadata requestHeadersToObjectMetadata(APIGatewayV2HTTPEvent request);
}
