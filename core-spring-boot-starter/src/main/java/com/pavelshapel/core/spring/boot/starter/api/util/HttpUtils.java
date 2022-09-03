package com.pavelshapel.core.spring.boot.starter.api.util;

import com.pavelshapel.core.spring.boot.starter.api.model.Request;

import java.io.InputStream;

public interface HttpUtils {
    InputStream requestBodyToInputStream(Request request, boolean isBase64);
}
