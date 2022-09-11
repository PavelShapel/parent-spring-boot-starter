package com.pavelshapel.core.spring.boot.starter.api.util;

import com.pavelshapel.core.spring.boot.starter.impl.model.properties.StringProperties;

import java.io.InputStream;

public interface SubstitutionUtils {
    String replace(InputStream source, StringProperties properties);

    String replace(String source, StringProperties properties);

    String replace(String source, String... properties);
}
