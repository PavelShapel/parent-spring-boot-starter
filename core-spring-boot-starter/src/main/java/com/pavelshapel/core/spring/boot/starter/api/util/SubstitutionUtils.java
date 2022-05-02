package com.pavelshapel.core.spring.boot.starter.api.util;

import java.io.InputStream;

public interface SubstitutionUtils {
    String replace(InputStream source, SubstitutionProperties properties);

    String replace(String source, SubstitutionProperties properties);
}
