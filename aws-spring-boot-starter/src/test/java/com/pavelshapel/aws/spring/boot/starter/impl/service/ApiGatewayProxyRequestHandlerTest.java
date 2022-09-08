package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@ExtendWith(MockitoExtension.class)
class ApiGatewayProxyRequestHandlerTest {
    @Spy
    ApiGatewayProxyRequestHandler requestHandler;

    @Test
    void isRequestMethod_WithValidParametersAndEqualsMethods_ShouldReturnTrue() {
        APIGatewayProxyRequestEvent request = createRequestWithHttpMethod(POST);

        boolean result = requestHandler.isRequestMethod(request, POST);

        assertThat(result).isTrue();
    }

    @Test
    void isRequestMethod_WithValidParametersAndNotEqualsMethods_ShouldReturnFalse() {
        APIGatewayProxyRequestEvent request = createRequestWithHttpMethod(POST);

        boolean result = requestHandler.isRequestMethod(request, GET);

        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @NullSource
    void isRequestMethod_WithNullRequestAsParameter_ShouldThrowException(APIGatewayProxyRequestEvent request) {
        assertThatThrownBy(() -> requestHandler.isRequestMethod(request, POST))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullSource
    void isRequestMethod_WithNullHttpMethodAsParameter_ShouldThrowException(HttpMethod httpMethod) {
        APIGatewayProxyRequestEvent request = createRequestWithHttpMethod(GET);

        assertThatThrownBy(() -> requestHandler.isRequestMethod(request, httpMethod))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void isPostMethod_WithValidParametersAndEqualsMethods_ShouldReturnTrue() {
        APIGatewayProxyRequestEvent request = createRequestWithHttpMethod(POST);

        boolean result = requestHandler.isPostMethod(request);

        assertThat(result).isTrue();
    }

    @Test
    void isPostMethod_WithValidParametersAndNotEqualsMethods_ShouldReturnFalse() {
        APIGatewayProxyRequestEvent request = createRequestWithHttpMethod(GET);

        boolean result = requestHandler.isPostMethod(request);

        assertThat(result).isFalse();
    }

    @Test
    void isGetMethod_WithValidParametersAndEqualsMethods_ShouldReturnTrue() {
        APIGatewayProxyRequestEvent request = createRequestWithHttpMethod(GET);

        boolean result = requestHandler.isGetMethod(request);

        assertThat(result).isTrue();
    }

    @Test
    void isGetMethod_WithValidParametersAndNotEqualsMethods_ShouldReturnFalse() {
        APIGatewayProxyRequestEvent request = createRequestWithHttpMethod(POST);

        boolean result = requestHandler.isGetMethod(request);

        assertThat(result).isFalse();
    }

    private APIGatewayProxyRequestEvent createRequestWithHttpMethod(HttpMethod httpMethod) {
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        APIGatewayProxyRequestEvent.ProxyRequestContext requestContext = new APIGatewayProxyRequestEvent.ProxyRequestContext();
        requestContext.setHttpMethod(httpMethod.name());
        request.withRequestContext(requestContext);
        return request;
    }
}