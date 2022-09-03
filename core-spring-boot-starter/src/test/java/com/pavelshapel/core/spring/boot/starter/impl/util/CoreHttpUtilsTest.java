package com.pavelshapel.core.spring.boot.starter.impl.util;

import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import com.pavelshapel.core.spring.boot.starter.api.model.Request;
import com.pavelshapel.core.spring.boot.starter.api.util.HttpUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {CoreStarterAutoConfiguration.class})
class CoreHttpUtilsTest {
    @Autowired
    private HttpUtils httpUtils;

    @Test
    void requestBodyToInputStream_WithNullRequest_ShouldTrowException() {
        assertThatThrownBy(() -> {
            try (InputStream inputStream = requestBodyToInputStream(null, true)) {
                return;
            }
        }).isInstanceOf(NullPointerException.class);
    }

    private InputStream requestBodyToInputStream(Request request, boolean isBase64) {
        return httpUtils.requestBodyToInputStream(request, isBase64);
    }
}