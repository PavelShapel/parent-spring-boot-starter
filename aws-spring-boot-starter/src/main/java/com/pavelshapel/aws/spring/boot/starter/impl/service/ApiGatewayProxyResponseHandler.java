package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.pavelshapel.aws.spring.boot.starter.api.service.ResponseHandler;
import com.pavelshapel.json.spring.boot.starter.converter.JsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.singletonMap;
import static java.util.Objects.nonNull;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

public class ApiGatewayProxyResponseHandler implements ResponseHandler {
    @Autowired
    JsonConverter jsonConverter;

    @Override
    public APIGatewayProxyResponseEvent updateResponseWithOkRequestAndGet(APIGatewayProxyResponseEvent response,
                                                                          String responseBody) {
        return verifiedResponseWithBodyAndStatusCode(response, responseBody, OK);
    }

    @Override
    public APIGatewayProxyResponseEvent updateResponseWithBadRequestAndGet(APIGatewayProxyResponseEvent response,
                                                                           Exception exception) {
        return Optional.ofNullable(exception)
                .map(Throwable::getMessage)
                .map(exceptionMessage -> singletonMap("exceptionMessage", exceptionMessage))
                .map(jsonConverter::pojoToJson)
                .map(responseBody -> verifiedResponseWithBodyAndStatusCode(response, responseBody, BAD_REQUEST))
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public APIGatewayProxyResponseEvent updateResponseWithBadRequestAndGet(APIGatewayProxyResponseEvent response,
                                                                           List<HttpMethod> supportedHttpMethods) {
        return Optional.ofNullable(supportedHttpMethods)
                .map(this::getUnsupportedHttpMethods)
                .map(Collection::stream)
                .map(stream -> stream.map(Enum::name))
                .map(stream -> stream.collect(Collectors.joining(", ")))
                .map(responseBody -> verifiedResponseWithBodyAndStatusCode(response, String.format("[%s] method(s) not supported", responseBody), BAD_REQUEST))
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<HttpMethod> getUnsupportedHttpMethods(List<HttpMethod> supportedHttpMethods) {
        List<HttpMethod> allHttpMethods = getAllHttpMethods();
        allHttpMethods.removeAll(supportedHttpMethods);
        return allHttpMethods;
    }

    private List<HttpMethod> getAllHttpMethods() {
        return Arrays.stream(HttpMethod.values())
                .collect(Collectors.toList());
    }

    private APIGatewayProxyResponseEvent verifiedResponseWithBodyAndStatusCode(APIGatewayProxyResponseEvent response,
                                                                               String responseBody,
                                                                               HttpStatus statusCode) {
        return Optional.ofNullable(responseBody)
                .filter(unused -> nonNull(response))
                .filter(unused -> nonNull(statusCode))
                .map(body -> responseWithBodyAndStatusCode(response, body, statusCode))
                .orElseThrow(IllegalArgumentException::new);
    }

    private APIGatewayProxyResponseEvent responseWithBodyAndStatusCode(APIGatewayProxyResponseEvent response,
                                                                       String responseBody,
                                                                       HttpStatus statusCode) {
        return response
                .withBody(responseBody)
                .withStatusCode(statusCode.value());
    }
}
