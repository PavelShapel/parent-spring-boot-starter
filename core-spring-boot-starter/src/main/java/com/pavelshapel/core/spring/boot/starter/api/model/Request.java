package com.pavelshapel.core.spring.boot.starter.api.model;

import java.util.Map;

public interface Request {
    String BODY_FIELD = "body";
    String HEADERS_FIELD = "headers";
    String IS_BASE_64_FIELD = "isBase64";

    String getBody();

    void setBody(String body);

    Map<String, String> getHeaders();

    void setHeaders(Map<String, String> headers);

    Boolean getIsBase64();

    void setIsBase64(Boolean isBase64);
}
