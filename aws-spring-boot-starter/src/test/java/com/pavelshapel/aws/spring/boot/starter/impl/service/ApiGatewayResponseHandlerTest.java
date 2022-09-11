package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.pavelshapel.aws.spring.boot.starter.AwsStarterAutoConfiguration;
import com.pavelshapel.aws.spring.boot.starter.api.service.ResponseHandler;
import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import com.pavelshapel.core.spring.boot.starter.api.util.ExceptionUtils;
import com.pavelshapel.json.spring.boot.starter.JsonStarterAutoConfiguration;
import com.pavelshapel.json.spring.boot.starter.converter.JsonConverter;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpMethod;

import java.util.List;

import static com.pavelshapel.aws.spring.boot.starter.api.service.ResponseHandler.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(classes = {
        AwsStarterAutoConfiguration.class,
        CoreStarterAutoConfiguration.class,
        JsonStarterAutoConfiguration.class
})
@FieldDefaults(level = AccessLevel.PRIVATE)
class ApiGatewayResponseHandlerTest {
    private static final String BODY = "body";
    private static final String THROWABLE_JSON = String.format("{\"%1$s\":\"%1$s\"}", EXCEPTION);
    private static final String VALID_JSON = String.format("{\"%1$s\":\"%1$s\"}", RESULT);
    private static final Exception THROWABLE = new Exception(EXCEPTION);
    private static final List<HttpMethod> SUPPORTED_HTTP_METHOD_LIST = List.of(GET, POST);
    @SpyBean
    JsonConverter jsonConverter;
    @MockBean
    ExceptionUtils exceptionUtils;
    @Autowired
    ResponseHandler responseHandler;

    @Test
    void updateResponseWithOkAndGet_WithValidJsonAsBodyAndValidParameters_ShouldUpdateAndReturnResponse() {
        APIGatewayV2HTTPResponse response = new APIGatewayV2HTTPResponse();
        String responseBody = VALID_JSON;

        APIGatewayV2HTTPResponse updatedResponse = responseHandler.updateResponseWithOkAndGet(response, responseBody);

        assertThat(updatedResponse)
                .isNotNull()
                .hasFieldOrPropertyWithValue(STATUS_CODE, OK.value())
                .hasFieldOrPropertyWithValue(BODY, responseBody);
    }

    @Test
    void updateResponseWithOkAndGet_WithInvalidJsonAsBodyAndValidParameters_ShouldUpdateAndReturnResponse() {
        APIGatewayV2HTTPResponse response = new APIGatewayV2HTTPResponse();

        APIGatewayV2HTTPResponse updatedResponse = responseHandler.updateResponseWithOkAndGet(response, RESULT);

        assertThat(updatedResponse)
                .isNotNull()
                .hasFieldOrPropertyWithValue(STATUS_CODE, OK.value())
                .hasFieldOrPropertyWithValue(BODY, VALID_JSON);
    }

    @ParameterizedTest
    @NullSource
    void updateResponseWithOkAndGet_WithNullResponseBodyAsParameter_ShouldThrowException(String responseBody) {
        mockExceptionUtilsCreateIllegalArgumentException();
        APIGatewayV2HTTPResponse response = new APIGatewayV2HTTPResponse();

        assertThatThrownBy(() -> responseHandler.updateResponseWithOkAndGet(response, responseBody))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullSource
    void updateResponseWithOkAndGet_WithNullResponseAsParameter_ShouldThrowException(APIGatewayV2HTTPResponse response) {
        mockExceptionUtilsCreateIllegalArgumentException();

        assertThatThrownBy(() -> responseHandler.updateResponseWithOkAndGet(response, RESPONSE_BODY))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateResponseWithBadRequestAndGet_Exception_WithValidParameters_ShouldUpdateAndReturnResponse() {
        APIGatewayV2HTTPResponse response = new APIGatewayV2HTTPResponse();

        APIGatewayV2HTTPResponse updatedResponse = responseHandler.updateResponseWithBadRequestAndGet(response, THROWABLE);

        assertThat(updatedResponse)
                .isNotNull()
                .hasFieldOrPropertyWithValue(STATUS_CODE, BAD_REQUEST.value())
                .hasFieldOrPropertyWithValue(BODY, THROWABLE_JSON);
    }

    @ParameterizedTest
    @NullSource
    void updateResponseWithBadRequestAndGet_Exception_WithNullExceptionAsParameter_ShouldThrowException(Exception exception) {
        mockExceptionUtilsCreateIllegalArgumentException();
        APIGatewayV2HTTPResponse response = new APIGatewayV2HTTPResponse();

        assertThatThrownBy(() -> responseHandler.updateResponseWithBadRequestAndGet(response, exception))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullSource
    void updateResponseWithBadRequestAndGet_Exception_WithNullResponseAsParameter_ShouldThrowException(APIGatewayV2HTTPResponse response) {
        mockExceptionUtilsCreateIllegalArgumentException();

        assertThatThrownBy(() -> responseHandler.updateResponseWithBadRequestAndGet(response, THROWABLE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateResponseWithBadRequestAndGet_SupportedMethods_WithValidParameters_ShouldUpdateAndReturnResponse() {
        APIGatewayV2HTTPResponse response = new APIGatewayV2HTTPResponse();

        APIGatewayV2HTTPResponse updatedResponse = responseHandler.updateResponseWithBadRequestAndGet(response, SUPPORTED_HTTP_METHOD_LIST);

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
        mockExceptionUtilsCreateIllegalArgumentException();
        APIGatewayV2HTTPResponse response = new APIGatewayV2HTTPResponse();

        assertThatThrownBy(() -> responseHandler.updateResponseWithBadRequestAndGet(response, httpMethods))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullSource
    void updateResponseWithBadRequestAndGet_SupportedMethods_WithNullResponseAsParameter_ShouldThrowException(APIGatewayV2HTTPResponse response) {
        mockExceptionUtilsCreateIllegalArgumentException();

        assertThatThrownBy(() -> responseHandler.updateResponseWithBadRequestAndGet(response, SUPPORTED_HTTP_METHOD_LIST))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @SuppressWarnings("ThrowableNotThrown")
    private void mockExceptionUtilsCreateIllegalArgumentException() {
        doReturn(new IllegalArgumentException()).when(exceptionUtils).createIllegalArgumentException(any());
    }
}