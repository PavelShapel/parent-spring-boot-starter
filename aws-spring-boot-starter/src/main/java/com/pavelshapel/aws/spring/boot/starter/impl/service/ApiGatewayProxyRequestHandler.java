package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.pavelshapel.aws.spring.boot.starter.api.service.RequestHandler;
import org.springframework.http.HttpMethod;

import java.util.Optional;

import static java.util.Objects.nonNull;

public class ApiGatewayProxyRequestHandler implements RequestHandler {
    @Override
    public boolean isRequestMethod(APIGatewayProxyRequestEvent request, HttpMethod httpMethod) {
        return Optional.ofNullable(httpMethod)
                .filter(unused -> nonNull(request))
                .map(Enum::name)
                .map(httpMethodName -> request.getRequestContext().getHttpMethod().equalsIgnoreCase(httpMethodName))
                .orElseThrow(IllegalArgumentException::new);
    }
}
