package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.pavelshapel.aws.spring.boot.starter.api.service.ResponseHandler;
import com.pavelshapel.core.spring.boot.starter.api.util.ExceptionUtils;
import com.pavelshapel.json.spring.boot.starter.converter.JsonConverter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.singletonMap;
import static java.util.Objects.nonNull;
import static java.util.function.Predicate.not;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiGatewayResponseHandler implements ResponseHandler {
    @Autowired
    JsonConverter jsonConverter;
    @Autowired
    ExceptionUtils exceptionUtils;

    public APIGatewayV2HTTPResponse updateResponseWithOkAndGet(APIGatewayV2HTTPResponse response,
                                                               String responseBody) {
        return verifiedResponseWithBodyAndStatusCode(response, responseBody, OK);
    }

    @Override
    public APIGatewayV2HTTPResponse updateResponseWithBadRequestAndGet(APIGatewayV2HTTPResponse response,
                                                                       Exception exception) {
        return Optional.ofNullable(exception)
                .map(Throwable::getMessage)
                .map(this::createExceptionResponseBody)
                .map(jsonConverter::pojoToJson)
                .map(responseBody -> verifiedResponseWithBodyAndStatusCode(response, responseBody, BAD_REQUEST))
                .orElseThrow(() -> exceptionUtils.createIllegalArgumentException(
                        RESPONSE, response,
                        EXCEPTION, exception
                ));
    }

    @Override
    public APIGatewayV2HTTPResponse updateResponseWithBadRequestAndGet(APIGatewayV2HTTPResponse response,
                                                                       List<HttpMethod> supportedHttpMethods) {
        return Optional.ofNullable(supportedHttpMethods)
                .map(this::getUnsupportedHttpMethods)
                .map(Collection::stream)
                .map(stream -> stream.map(Enum::name))
                .map(stream -> stream.collect(Collectors.joining(", ")))
                .map(responseBody -> String.format(METHOD_NOT_SUPPORTED_PATTERN, responseBody))
                .map(this::createExceptionResponseBody)
                .map(jsonConverter::pojoToJson)
                .map(responseBody -> verifiedResponseWithBodyAndStatusCode(response, responseBody, BAD_REQUEST))
                .orElseThrow(() -> exceptionUtils.createIllegalArgumentException(
                        RESPONSE, response,
                        SUPPORTED_HTTP_METHODS, supportedHttpMethods
                ));
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

    private APIGatewayV2HTTPResponse verifiedResponseWithBodyAndStatusCode(APIGatewayV2HTTPResponse response,
                                                                           String responseBody,
                                                                           HttpStatus statusCode) {
        return Optional.ofNullable(responseBody)
                .filter(unused -> nonNull(response))
                .filter(unused -> nonNull(statusCode))
                .map(this::updateIfSimpleResponse)
                .map(body -> responseWithBodyAndStatusCode(response, body, statusCode))
                .orElseThrow(() -> exceptionUtils.createIllegalArgumentException(
                        RESPONSE, response,
                        RESPONSE_BODY, responseBody,
                        STATUS_CODE, statusCode
                ));
    }

    private String updateIfSimpleResponse(String responseBody) {
        return Optional.of(responseBody)
                .filter(not(jsonConverter::isValidJson))
                .map(this::createResultResponseBody)
                .map(jsonConverter::pojoToJson)
                .orElse(responseBody);
    }

    private Map<String, String> createResultResponseBody(String value) {
        return createSimpleResponseBody(RESULT, value);
    }

    private Map<String, String> createExceptionResponseBody(String value) {
        return createSimpleResponseBody(EXCEPTION, value);
    }

    private Map<String, String> createSimpleResponseBody(String key, String value) {
        return singletonMap(key, value);
    }

    private APIGatewayV2HTTPResponse responseWithBodyAndStatusCode(APIGatewayV2HTTPResponse response,
                                                                   String responseBody,
                                                                   HttpStatus statusCode) {
        response.setBody(responseBody);
        response.setStatusCode(statusCode.value());
        return response;
    }
}
