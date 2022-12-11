package com.pavelshapel.core.spring.boot.starter.api.util;

import java.io.InputStream;

public interface FileUtils {
    String getContentType(InputStream inputStream);
}
