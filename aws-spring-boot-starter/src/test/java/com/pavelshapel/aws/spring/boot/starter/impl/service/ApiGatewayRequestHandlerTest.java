package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.tests.annotations.Event;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.pavelshapel.aws.spring.boot.starter.AbstractTest;
import com.pavelshapel.aws.spring.boot.starter.api.service.RequestHandler;
import com.pavelshapel.aws.spring.boot.starter.provider.OneStringProvider;
import com.pavelshapel.aws.spring.boot.starter.provider.TwoStringProvider;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@FieldDefaults(level = AccessLevel.PRIVATE)
class ApiGatewayRequestHandlerTest extends AbstractTest {
    @Autowired
    RequestHandler requestHandler;

    @Test
    void isRequestMethod_WithValidParametersAndEqualsMethods_ShouldReturnTrue() {
        APIGatewayV2HTTPEvent request = createRequestWithHttpMethod(POST);

        boolean result = requestHandler.isRequestMethod(request, POST);

        assertThat(result).isTrue();
    }

    @Test
    void isRequestMethod_WithValidParametersAndNotEqualsMethods_ShouldReturnFalse() {
        APIGatewayV2HTTPEvent request = createRequestWithHttpMethod(POST);

        boolean result = requestHandler.isRequestMethod(request, GET);

        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @NullSource
    void isRequestMethod_WithNullRequestAsParameter_ShouldThrowException(APIGatewayV2HTTPEvent request) {
        assertThatThrownBy(() -> requestHandler.isRequestMethod(request, POST))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullSource
    void isRequestMethod_WithNullHttpMethodAsParameter_ShouldThrowException(HttpMethod httpMethod) {
        APIGatewayV2HTTPEvent request = createRequestWithHttpMethod(GET);

        assertThatThrownBy(() -> requestHandler.isRequestMethod(request, httpMethod))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void isPostMethod_WithValidParametersAndEqualsMethods_ShouldReturnTrue() {
        APIGatewayV2HTTPEvent request = createRequestWithHttpMethod(POST);

        boolean result = requestHandler.isPostMethod(request);

        assertThat(result).isTrue();
    }

    @Test
    void isPostMethod_WithValidParametersAndNotEqualsMethods_ShouldReturnFalse() {
        APIGatewayV2HTTPEvent request = createRequestWithHttpMethod(GET);

        boolean result = requestHandler.isPostMethod(request);

        assertThat(result).isFalse();
    }

    @Test
    void isGetMethod_WithValidParametersAndEqualsMethods_ShouldReturnTrue() {
        APIGatewayV2HTTPEvent request = createRequestWithHttpMethod(GET);

        boolean result = requestHandler.isGetMethod(request);

        assertThat(result).isTrue();
    }

    @Test
    void isGetMethod_WithValidParametersAndNotEqualsMethods_ShouldReturnFalse() {
        APIGatewayV2HTTPEvent request = createRequestWithHttpMethod(POST);

        boolean result = requestHandler.isGetMethod(request);

        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @ArgumentsSource(TwoStringProvider.class)
    void getQueryParameter_WithValidParametersAndExistingQueryParameter_ShouldReturnQueryParameterValue(String key, String value) {
        APIGatewayV2HTTPEvent request = createRequestWithHttpMethod(POST, Map.of(key, value));

        String result = requestHandler.getQueryParameter(request, key);

        assertThat(result).isEqualTo(value);
    }

    @ParameterizedTest
    @ArgumentsSource(TwoStringProvider.class)
    void getQueryParameter_WithValidParametersAndNotExistingQueryParameter_ShouldThrowException(String key, String value) {
        APIGatewayV2HTTPEvent request = createRequestWithHttpMethod(POST);

        assertThatThrownBy(() -> requestHandler.getQueryParameter(request, key))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void getQueryParameter_WithNullRequestAsParameter_ShouldThrowException(String key) {
        assertThatThrownBy(() -> requestHandler.getQueryParameter(null, key))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(TwoStringProvider.class)
    void getQueryParameter_WithNullQueryParameterAsParameter_ShouldThrowException(String key, String value) {
        APIGatewayV2HTTPEvent request = createRequestWithHttpMethod(POST, Map.of(key, value));

        assertThatThrownBy(() -> requestHandler.getQueryParameter(request, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @Event(value = "src/test/resources/request_base64.json", type = APIGatewayV2HTTPEvent.class)
    @SneakyThrows
    void requestBodyToInputStream_WithBase64BodyParameter_ShouldReturnInputStream(APIGatewayV2HTTPEvent request) {
        try (InputStream result = requestHandler.requestBodyToInputStream(request)) {
            assertThat(result)
                    .isNotNull();
        }
    }

    @ParameterizedTest
    @Event(value = "src/test/resources/request.json", type = APIGatewayV2HTTPEvent.class)
    @SneakyThrows
    void requestBodyToInputStream_WithPlainTextBodyParameter_ShouldReturnInputStream(APIGatewayV2HTTPEvent request) {
        try (InputStream result = requestHandler.requestBodyToInputStream(request)) {
            assertThat(result)
                    .isNotNull()
                    .hasSameContentAs(new ByteArrayInputStream("test".getBytes()));
        }
    }

    @ParameterizedTest
    @Event(value = "src/test/resources/request.json", type = APIGatewayV2HTTPEvent.class)
    @SneakyThrows
    void requestHeadersToObjectMetadata_WithPlainTextBodyParameter_ShouldReturnInputStream(APIGatewayV2HTTPEvent request) {
        ObjectMetadata result = requestHandler.requestHeadersToObjectMetadata(request);

        assertThat(result)
                .isNotNull()
                .isInstanceOf(ObjectMetadata.class)
                .extracting(ObjectMetadata::getContentType)
                .asString()
                .isEqualTo(TEXT_PLAIN_VALUE);
    }


    private APIGatewayV2HTTPEvent createRequestWithHttpMethod(HttpMethod httpMethod) {
        return createRequestWithHttpMethod(httpMethod, null);
    }

    private APIGatewayV2HTTPEvent createRequestWithHttpMethod(HttpMethod httpMethod, Map<String, String> queryStringParameters) {
        return Optional.of(httpMethod)
                .map(HttpMethod::name)
                .map(httpMethodName -> APIGatewayV2HTTPEvent.RequestContext.Http.builder()
                        .withMethod(httpMethodName)
                        .build())
                .map(http -> APIGatewayV2HTTPEvent.RequestContext.builder()
                        .withHttp(http)
                        .build())
                .map(requestContext -> APIGatewayV2HTTPEvent.builder()
                        .withRequestContext(requestContext)
                        .withQueryStringParameters(queryStringParameters)
                        .build())
                .orElseThrow();
    }
}