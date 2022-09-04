package com.pavelshapel.aws.spring.boot.starter.api.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.springframework.http.HttpMethod;

import java.util.List;

public interface ResponseHandler {
    APIGatewayProxyResponseEvent updateResponseWithOkRequestAndGet(APIGatewayProxyResponseEvent response, String responseBody);

    APIGatewayProxyResponseEvent updateResponseWithBadRequestAndGet(APIGatewayProxyResponseEvent response, Exception exception);

    APIGatewayProxyResponseEvent updateResponseWithBadRequestAndGet(APIGatewayProxyResponseEvent response, List<HttpMethod> httpMethods);
}
