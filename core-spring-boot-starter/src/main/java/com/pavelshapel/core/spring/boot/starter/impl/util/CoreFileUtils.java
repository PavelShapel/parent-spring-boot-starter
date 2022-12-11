package com.pavelshapel.core.spring.boot.starter.impl.util;

import com.pavelshapel.core.spring.boot.starter.api.util.FileUtils;
import lombok.SneakyThrows;
import org.apache.tika.Tika;

import java.io.InputStream;

public class CoreFileUtils implements FileUtils {
    @Override
    @SneakyThrows
    public String getContentType(InputStream inputStream) {
        Tika tika = new Tika();
        return tika.detect(inputStream);
    }
}
