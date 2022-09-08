package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.pavelshapel.core.spring.boot.starter.api.util.ExceptionUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class ApiGatewayProxyRequestHandlerTest {
    @Mock
    ExceptionUtils exceptionUtils;
    @InjectMocks
    ApiGatewayProxyRequestHandler requestHandler;

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
        doReturn(new IllegalArgumentException()).when(exceptionUtils).createIllegalArgumentException(anyMap());

        assertThatThrownBy(() -> requestHandler.isRequestMethod(request, POST))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullSource
    void isRequestMethod_WithNullHttpMethodAsParameter_ShouldThrowException(HttpMethod httpMethod) {
        doReturn(new IllegalArgumentException()).when(exceptionUtils).createIllegalArgumentException(anyMap());
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

    private APIGatewayV2HTTPEvent createRequestWithHttpMethod(HttpMethod httpMethod) {
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
                        .build())
                .orElseThrow();
    }
}