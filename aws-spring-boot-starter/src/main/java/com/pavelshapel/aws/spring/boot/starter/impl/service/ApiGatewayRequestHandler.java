package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.pavelshapel.aop.spring.boot.starter.annotation.ExceptionWrapped;
import com.pavelshapel.aws.spring.boot.starter.api.service.RequestHandler;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpMethod;

import java.util.Optional;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExceptionWrapped
public class ApiGatewayRequestHandler implements RequestHandler {
    @Override
    public boolean isRequestMethod(APIGatewayV2HTTPEvent request, HttpMethod httpMethod) {
        return Optional.ofNullable(httpMethod)
                .filter(unused -> nonNull(request))
                .map(Enum::name)
                .map(httpMethodName -> getRequestHttpMethod(request).equalsIgnoreCase(httpMethodName))
                .orElseThrow();
    }

    private String getRequestHttpMethod(APIGatewayV2HTTPEvent request) {
        return Optional.ofNullable(request)
                .map(APIGatewayV2HTTPEvent::getRequestContext)
                .map(APIGatewayV2HTTPEvent.RequestContext::getHttp)
                .map(APIGatewayV2HTTPEvent.RequestContext.Http::getMethod)
                .orElseThrow();
    }

    @Override
    public String getQueryParameter(APIGatewayV2HTTPEvent request, String queryParameter) {
        return Optional.ofNullable(request)
                .filter(unused -> isNoneBlank(queryParameter))
                .map(APIGatewayV2HTTPEvent::getQueryStringParameters)
                .map(map -> map.get(queryParameter))
                .orElseThrow();
    }
}
