package com.pavelshapel.core.spring.boot.starter.impl.util;

import com.pavelshapel.core.spring.boot.starter.api.model.Request;
import com.pavelshapel.core.spring.boot.starter.api.util.HttpUtils;
import com.pavelshapel.core.spring.boot.starter.api.util.StreamUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.MultipartStream;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

public class CoreHttpUtils implements HttpUtils {
    @Autowired
    StreamUtils streamUtils;
    public static final String BOUNDARY = "boundary";
    public static final String BOUNDARY_EQUALS = BOUNDARY + "=";

    @Override
    public InputStream requestBodyToInputStream(Request request, boolean isBase64) {
        return Optional.of(isBase64)
                .filter(Boolean.TRUE::equals)
                .map(unused -> base64ToInputStream(request))
                .orElseGet(() -> plainTextToInputStream(request));
    }

    private ByteArrayInputStream plainTextToInputStream(Request request) {
        return new ByteArrayInputStream(getBody(request));
    }

    private ByteArrayInputStream base64ToInputStream(Request request) {
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

    private byte[] getDecodedBody(Request request) {
        return Base64.getDecoder().decode(getBody(request));
    }

    private byte[] getBody(Request request) {
        return request.getBody().getBytes(StandardCharsets.UTF_8);
    }

    private Optional<String> getBoundary(Map<String, String> headers) {
        return Optional.of(headers.get(CONTENT_TYPE))
                .map(contentTypeHeader -> List.of(contentTypeHeader.split(";")))
                .orElse(Collections.emptyList())
                .stream()
                .filter(parameter -> parameter.startsWith(BOUNDARY_EQUALS))
                .map(parameter -> parameter.replace(BOUNDARY_EQUALS, StringUtils.EMPTY))
                .collect(streamUtils.toSingleton());
    }
}
