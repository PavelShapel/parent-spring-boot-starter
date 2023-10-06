package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.pavelshapel.aop.spring.boot.starter.annotation.ExceptionWrapped;
import com.pavelshapel.aws.spring.boot.starter.api.service.ResponseHandler;
import com.pavelshapel.json.spring.boot.starter.converter.JsonConverter;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.amazonaws.util.IOUtils.toByteArray;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.singletonMap;
import static java.util.Objects.nonNull;
import static java.util.function.Predicate.not;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExceptionWrapped
public class ApiGatewayResponseHandler implements ResponseHandler {
    public static final String EXCEPTION = "exception";
    public static final String RESULT = "result";
    private static final String METHOD_NOT_SUPPORTED_PATTERN = "[%s] method(s) not supported";
    @Autowired
    JsonConverter jsonConverter;

    public APIGatewayV2HTTPResponse updateResponseWithOkAndGet(APIGatewayV2HTTPResponse response, String responseBody) {
        return verifiedResponseWithBodyAndStatusCode(response, responseBody, OK);
    }

    @SneakyThrows
    @Override
    public APIGatewayV2HTTPResponse updateResponseWithOkAndGet(APIGatewayV2HTTPResponse response, S3Object s3Object) {
        ObjectMetadata objectMetadata = s3Object.getObjectMetadata();
        String fileName = URLEncoder.encode(s3Object.getKey(), UTF_8);
        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, objectMetadata.getContentType());
        headers.put(CONTENT_LENGTH, String.valueOf(objectMetadata.getContentLength()));
        headers.put(CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", fileName));
        response.setHeaders(headers);
        try (S3ObjectInputStream objectContent = s3Object.getObjectContent()) {
            response.setBody(Base64.getEncoder().encodeToString(toByteArray(objectContent)));
        }
        response.setIsBase64Encoded(true);
        response.setStatusCode(OK.value());
        return response;
    }

    @Override
    public APIGatewayV2HTTPResponse updateResponseWithBadRequestAndGet(APIGatewayV2HTTPResponse response,
                                                                       Exception exception) {
        return Optional.ofNullable(exception)
                .map(Throwable::getMessage)
                .map(this::createExceptionResponseBody)
                .map(jsonConverter::pojoToJson)
                .map(responseBody -> verifiedResponseWithBodyAndStatusCode(response, responseBody, BAD_REQUEST))
                .orElseThrow();
    }

    @Override
    public APIGatewayV2HTTPResponse updateResponseWithBadRequestAndGet(APIGatewayV2HTTPResponse response,
                                                                       List<HttpMethod> supportedHttpMethods) {
        return Optional.ofNullable(supportedHttpMethods)
                .map(this::getUnsupportedHttpMethods)
                .map(Collection::stream)
                .map(stream -> stream.map(HttpMethod::name))
                .map(stream -> stream.collect(Collectors.joining(", ")))
                .map(METHOD_NOT_SUPPORTED_PATTERN::formatted)
                .map(this::createExceptionResponseBody)
                .map(jsonConverter::pojoToJson)
                .map(responseBody -> verifiedResponseWithBodyAndStatusCode(response, responseBody, BAD_REQUEST))
                .orElseThrow();
    }

    private List<HttpMethod> getUnsupportedHttpMethods(List<HttpMethod> supportedHttpMethods) {
        List<HttpMethod> allHttpMethods = getAllHttpMethods();
        allHttpMethods.removeAll(supportedHttpMethods);
        return allHttpMethods;
    }

    private List<HttpMethod> getAllHttpMethods() {
        return new ArrayList<>(List.of(HttpMethod.values()));
    }

    private APIGatewayV2HTTPResponse verifiedResponseWithBodyAndStatusCode(APIGatewayV2HTTPResponse response,
                                                                           String responseBody,
                                                                           HttpStatus statusCode) {
        return Optional.ofNullable(responseBody)
                .filter(unused -> nonNull(response))
                .filter(unused -> nonNull(statusCode))
                .map(this::updateIfSimpleResponse)
                .map(body -> responseWithBodyAndStatusCode(response, body, statusCode))
                .orElseThrow();
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
