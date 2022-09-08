package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.pavelshapel.aws.spring.boot.starter.api.service.RequestHandler;
import com.pavelshapel.core.spring.boot.starter.api.util.ExceptionUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.nonNull;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiGatewayProxyRequestHandler implements RequestHandler {
    @Autowired
    ExceptionUtils exceptionUtils;

    @Override
    public boolean isRequestMethod(APIGatewayV2HTTPEvent request, HttpMethod httpMethod) {
        return Optional.ofNullable(httpMethod)
                .filter(unused -> nonNull(request))
                .map(Enum::name)
                .map(httpMethodName -> getRequestHttpMethod(request).equalsIgnoreCase(httpMethodName))
                .orElseThrow(() -> exceptionUtils.createIllegalArgumentException(getRequestArgument(request)));
    }

    private String getRequestHttpMethod(APIGatewayV2HTTPEvent request) {
        return Optional.ofNullable(request)
                .map(APIGatewayV2HTTPEvent::getRequestContext)
                .map(APIGatewayV2HTTPEvent.RequestContext::getHttp)
                .map(APIGatewayV2HTTPEvent.RequestContext.Http::getMethod)
                .orElseThrow(() -> exceptionUtils.createIllegalArgumentException(getRequestArgument(request)));
    }

    private Map<String, Object> getRequestArgument(APIGatewayV2HTTPEvent request) {
        return Optional.ofNullable(request)
                .map(event -> new HashMap<String, Object>(Map.of("request", event)))
                .orElseGet(HashMap::new);
    }
}
