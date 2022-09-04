package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.pavelshapel.json.spring.boot.starter.converter.JsonConverter;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExtendWith(MockitoExtension.class)
class ApiGatewayProxyResponseHandlerTest {
    private static final String STATUS_CODE = "statusCode";
    private static final String BODY = "body";
    private static final String RESPONSE_BODY = "responseBody";
    private static final String EXCEPTION_MESSAGE = "exceptionMessage";
    private static final String EXCEPTION_JSON = String.format("{\"%1$s\":\"%1$s\"}", EXCEPTION_MESSAGE);
    private static final Exception EXCEPTION = new Exception(EXCEPTION_MESSAGE);
    private static final List<HttpMethod> SUPPORTED_HTTP_METHODS = List.of(GET, POST);
    @Mock
    JsonConverter jsonConverter;
    @InjectMocks
    ApiGatewayProxyResponseHandler responseHandler;

    @Test
    void updateResponseWithOkRequestAndGet_WithValidParameters_ShouldUpdateAndReturnResponse() {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        String responseBody = RESPONSE_BODY;

        APIGatewayProxyResponseEvent updatedResponse = responseHandler.updateResponseWithOkRequestAndGet(response, responseBody);

        assertThat(updatedResponse)
                .isNotNull()
                .hasFieldOrPropertyWithValue(STATUS_CODE, OK.value())
                .hasFieldOrPropertyWithValue(BODY, responseBody);
    }

    @ParameterizedTest
    @NullSource
    void updateResponseWithOkRequestAndGet_WithNullResponseBodyAsParameter_ShouldThrowException(String responseBody) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        assertThatThrownBy(() -> responseHandler.updateResponseWithOkRequestAndGet(response, responseBody))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullSource
    void updateResponseWithOkRequestAndGet_WithNullResponseAsParameter_ShouldThrowException(APIGatewayProxyResponseEvent response) {
        assertThatThrownBy(() -> responseHandler.updateResponseWithOkRequestAndGet(response, RESPONSE_BODY))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateResponseWithBadRequestAndGet_Exception_WithValidParameters_ShouldUpdateAndReturnResponse() {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        doReturn(EXCEPTION_JSON).when(jsonConverter).pojoToJson(anyMap());

        APIGatewayProxyResponseEvent updatedResponse = responseHandler.updateResponseWithBadRequestAndGet(response, EXCEPTION);

        assertThat(updatedResponse)
                .isNotNull()
                .hasFieldOrPropertyWithValue(STATUS_CODE, BAD_REQUEST.value())
                .hasFieldOrPropertyWithValue(BODY, EXCEPTION_JSON);
    }

    @ParameterizedTest
    @NullSource
    void updateResponseWithBadRequestAndGet_Exception_WithNullExceptionAsParameter_ShouldThrowException(Exception exception) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        assertThatThrownBy(() -> responseHandler.updateResponseWithBadRequestAndGet(response, exception))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullSource
    void updateResponseWithBadRequestAndGet_Exception_WithNullResponseAsParameter_ShouldThrowException(APIGatewayProxyResponseEvent response) {
        assertThatThrownBy(() -> responseHandler.updateResponseWithBadRequestAndGet(response, EXCEPTION))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateResponseWithBadRequestAndGet_SupportedMethods_WithValidParameters_ShouldUpdateAndReturnResponse() {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        APIGatewayProxyResponseEvent updatedResponse = responseHandler.updateResponseWithBadRequestAndGet(response, SUPPORTED_HTTP_METHODS);

        assertThat(updatedResponse)
                .isNotNull()
                .hasFieldOrPropertyWithValue(STATUS_CODE, BAD_REQUEST.value())
                .extracting(BODY)
                .asString()
                .doesNotContain(GET.name(), POST.name());
    }

    @ParameterizedTest
    @NullSource
    void updateResponseWithBadRequestAndGet_SupportedMethods_WithNullSupportedMethodsAsParameter_ShouldThrowException(List<HttpMethod> httpMethods) {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        assertThatThrownBy(() -> responseHandler.updateResponseWithBadRequestAndGet(response, httpMethods))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullSource
    void updateResponseWithBadRequestAndGet_SupportedMethods_WithNullResponseAsParameter_ShouldThrowException(APIGatewayProxyResponseEvent response) {
        assertThatThrownBy(() -> responseHandler.updateResponseWithBadRequestAndGet(response, SUPPORTED_HTTP_METHODS))
                .isInstanceOf(IllegalArgumentException.class);
    }
}