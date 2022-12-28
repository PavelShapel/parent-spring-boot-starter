package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.pavelshapel.aop.spring.boot.starter.annotation.ExceptionWrapped;
import com.pavelshapel.aws.spring.boot.starter.api.service.RequestHandler;
import com.pavelshapel.core.spring.boot.starter.api.util.StreamUtils;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.MultipartStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExceptionWrapped
public class ApiGatewayRequestHandler implements RequestHandler {
    @Autowired
    StreamUtils streamUtils;
    public static final String BOUNDARY = "boundary";
    public static final String BOUNDARY_EQUALS = BOUNDARY + "=";

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

    @Override
    public InputStream requestBodyToInputStream(APIGatewayV2HTTPEvent request) {
        return Optional.of(request)
                .map(APIGatewayV2HTTPEvent::getIsBase64Encoded)
                .filter(Boolean.TRUE::equals)
                .map(unused -> base64ToInputStream(request))
                .orElseGet(() -> plainTextToInputStream(request));
    }

    private ByteArrayInputStream plainTextToInputStream(APIGatewayV2HTTPEvent request) {
        return new ByteArrayInputStream(getBody(request));
    }

    private ByteArrayInputStream base64ToInputStream(APIGatewayV2HTTPEvent request) {
        byte[] decodedBody = getDecodedBody(request);
        return getBoundary(request.getHeaders())
                .map(boundary -> multipleFileToInputStream(decodedBody, boundary))
                .orElseGet(() -> new ByteArrayInputStream(decodedBody));
    }

    @SneakyThrows
    private ByteArrayInputStream multipleFileToInputStream(byte[] decodedBody, String boundary) {
        try (ByteArrayInputStream content = new ByteArrayInputStream(decodedBody); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            MultipartStream multipartStream = new MultipartStream(content, boundary.getBytes(StandardCharsets.UTF_8), decodedBody.length, null);
            boolean nextPart = multipartStream.skipPreamble();
            while (nextPart) {
                multipartStream.readHeaders();
                multipartStream.readBodyData(outputStream);
                nextPart = multipartStream.readBoundary();
            }
            return new ByteArrayInputStream(outputStream.toByteArray());
        }
    }

    private byte[] getDecodedBody(APIGatewayV2HTTPEvent request) {
        return Base64.getDecoder().decode(getBody(request));
    }

    private byte[] getBody(APIGatewayV2HTTPEvent request) {
        return request.getBody().getBytes(StandardCharsets.UTF_8);
    }

    private Optional<String> getBoundary(Map<String, String> headers) {
        return Optional.of(CONTENT_TYPE)
                .map(String::toLowerCase)
                .map(headers::get)
                .map(this::splitContentTypeHeader)
                .orElse(Collections.emptyList())
                .stream()
                .map(String::strip)
                .filter(parameter -> parameter.startsWith(BOUNDARY_EQUALS))
                .map(parameter -> parameter.replace(BOUNDARY_EQUALS, StringUtils.EMPTY))
                .collect(streamUtils.toSingleton());
    }

    private List<String> splitContentTypeHeader(String contentTypeHeader) {
        return List.of(contentTypeHeader.split(";"));
    }

    @Override
    public ObjectMetadata requestHeadersToObjectMetadata(APIGatewayV2HTTPEvent request) {
        return Optional.of(request)
                .map(APIGatewayV2HTTPEvent::getHeaders)
                .map(this::createObjectMetadata)
                .orElse(new ObjectMetadata());
    }

    private ObjectMetadata createObjectMetadata(Map<String, String> headers) {
        String contentType = headers.get(CONTENT_TYPE.toLowerCase());
        String contentLength = headers.get(CONTENT_LENGTH.toLowerCase());
        return createMetadata(contentType, contentLength);
    }

    private ObjectMetadata createMetadata(String contentType, String contentLength) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(Long.parseLong(contentLength));
        return objectMetadata;
    }
}
