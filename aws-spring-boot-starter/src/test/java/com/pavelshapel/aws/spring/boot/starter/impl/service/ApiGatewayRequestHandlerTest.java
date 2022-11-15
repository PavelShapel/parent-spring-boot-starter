package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.pavelshapel.aws.spring.boot.starter.AbstractTest;
import com.pavelshapel.aws.spring.boot.starter.api.service.RequestHandler;
import com.pavelshapel.aws.spring.boot.starter.provider.OneStringProvider;
import com.pavelshapel.aws.spring.boot.starter.provider.TwoStringProvider;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

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
    void getQueryParameter_WithValidParametersAndExistingQueryParameter_ShouldReturnQueryParameterValue(String bucketName, String key) {
        APIGatewayV2HTTPEvent request = createRequestWithHttpMethod(POST, Map.of(bucketName, key));

        String result = requestHandler.getQueryParameter(request, bucketName);

        assertThat(result).isEqualTo(key);
    }

    @ParameterizedTest
    @ArgumentsSource(TwoStringProvider.class)
    void getQueryParameter_WithValidParametersAndNotExistingQueryParameter_ShouldThrowException(String bucketName, String key) {
        APIGatewayV2HTTPEvent request = createRequestWithHttpMethod(POST);

        assertThatThrownBy(() -> requestHandler.getQueryParameter(request, bucketName))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(OneStringProvider.class)
    void getQueryParameter_WithNullRequestAsParameter_ShouldThrowException(String bucketName) {
        assertThatThrownBy(() -> requestHandler.getQueryParameter(null, bucketName))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ArgumentsSource(TwoStringProvider.class)
    void getQueryParameter_WithNullQueryParameterAsParameter_ShouldThrowException(String bucketName, String key) {
        APIGatewayV2HTTPEvent request = createRequestWithHttpMethod(POST, Map.of(bucketName, key));

        assertThatThrownBy(() -> requestHandler.getQueryParameter(request, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private APIGatewayV2HTTPEvent createRequestWithHttpMethod(HttpMethod httpMethod) {
        return createRequestWithHttpMethod(httpMethod, null);
    }

    private APIGatewayV2HTTPEvent createRequestWithHttpMethod(HttpMethod httpMethod, Map<String, String> queryStringParameters) {
        return Optional.of(httpMethod)
                .map(Enum::name)
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