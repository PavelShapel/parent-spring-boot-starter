package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.pavelshapel.aws.spring.boot.starter.AbstractTest;
import com.pavelshapel.aws.spring.boot.starter.api.service.ResponseHandler;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.io.ByteArrayInputStream;
import java.util.List;

import static com.pavelshapel.aws.spring.boot.starter.impl.service.ApiGatewayResponseHandler.EXCEPTION;
import static com.pavelshapel.aws.spring.boot.starter.impl.service.ApiGatewayResponseHandler.RESULT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@FieldDefaults(level = AccessLevel.PRIVATE)
class ApiGatewayResponseHandlerTest extends AbstractTest {
    private static final String BODY = "body";
    private static final String STATUS_CODE = "statusCode";
    private static final String RESPONSE_BODY = "responseBody";
    private static final String THROWABLE_JSON = String.format("{\"%1$s\":\"%1$s\"}", EXCEPTION);
    private static final String VALID_JSON = String.format("{\"%1$s\":\"%1$s\"}", RESULT);
    private static final Exception THROWABLE = new Exception(EXCEPTION);
    private static final List<HttpMethod> SUPPORTED_HTTP_METHOD_LIST = List.of(GET, POST);
    private static final String KEY = "key";
    private static final String TEST = "test";
    @Autowired
    ResponseHandler responseHandler;

    @Test
    void updateResponseWithOkJsonBodyAndGet_WithValidJsonAsBodyAndValidParameters_ShouldUpdateAndReturnResponse() {
        APIGatewayV2HTTPResponse response = new APIGatewayV2HTTPResponse();
        String responseBody = VALID_JSON;

        APIGatewayV2HTTPResponse updatedResponse = responseHandler.updateResponseWithOkAndGet(response, responseBody);

        assertThat(updatedResponse)
                .isNotNull()
                .hasFieldOrPropertyWithValue(STATUS_CODE, OK.value())
                .hasFieldOrPropertyWithValue(BODY, responseBody);
    }

    @Test
    void updateResponseWithOkJsonBodyAndGet_WithInvalidJsonAsBodyAndValidParameters_ShouldUpdateAndReturnResponse() {
        APIGatewayV2HTTPResponse response = new APIGatewayV2HTTPResponse();

        APIGatewayV2HTTPResponse updatedResponse = responseHandler.updateResponseWithOkAndGet(response, RESULT);

        assertThat(updatedResponse)
                .isNotNull()
                .hasFieldOrPropertyWithValue(STATUS_CODE, OK.value())
                .hasFieldOrPropertyWithValue(BODY, VALID_JSON);
    }

    @ParameterizedTest
    @NullSource
    void updateResponseWithOkJsonBodyAndGet_WithNullResponseBodyAsParameter_ShouldThrowException(String responseBody) {
        APIGatewayV2HTTPResponse response = new APIGatewayV2HTTPResponse();

        assertThatThrownBy(() -> responseHandler.updateResponseWithOkAndGet(response, responseBody))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullSource
    void updateResponseWithOkJsonBodyAndGet_WithNullResponseAsParameter_ShouldThrowException(APIGatewayV2HTTPResponse response) {
        assertThatThrownBy(() -> responseHandler.updateResponseWithOkAndGet(response, RESPONSE_BODY))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @SneakyThrows
    void updateResponseWithOkAndGet_WithS3ObjectAndValidParameters_ShouldUpdateAndReturnResponseWithOkStatusCode() {
        APIGatewayV2HTTPResponse response = new APIGatewayV2HTTPResponse();
        try (S3Object s3Object = new S3Object()) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(4);
            objectMetadata.setContentType(TEXT_PLAIN_VALUE);
            s3Object.setKey(KEY);
            s3Object.setObjectContent(new ByteArrayInputStream(TEST.getBytes()));
            s3Object.setObjectMetadata(objectMetadata);

            APIGatewayV2HTTPResponse updatedResponse = responseHandler.updateResponseWithOkAndGet(response, s3Object);

            assertThat(updatedResponse)
                    .isNotNull()
                    .hasFieldOrPropertyWithValue(STATUS_CODE, OK.value());
        }
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
        APIGatewayV2HTTPResponse response = new APIGatewayV2HTTPResponse();

        assertThatThrownBy(() -> responseHandler.updateResponseWithBadRequestAndGet(response, exception))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullSource
    void updateResponseWithBadRequestAndGet_Exception_WithNullResponseAsParameter_ShouldThrowException(APIGatewayV2HTTPResponse response) {
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
        APIGatewayV2HTTPResponse response = new APIGatewayV2HTTPResponse();

        assertThatThrownBy(() -> responseHandler.updateResponseWithBadRequestAndGet(response, httpMethods))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullSource
    void updateResponseWithBadRequestAndGet_SupportedMethods_WithNullResponseAsParameter_ShouldThrowException(APIGatewayV2HTTPResponse response) {
        assertThatThrownBy(() -> responseHandler.updateResponseWithBadRequestAndGet(response, SUPPORTED_HTTP_METHOD_LIST))
                .isInstanceOf(IllegalArgumentException.class);
    }
}