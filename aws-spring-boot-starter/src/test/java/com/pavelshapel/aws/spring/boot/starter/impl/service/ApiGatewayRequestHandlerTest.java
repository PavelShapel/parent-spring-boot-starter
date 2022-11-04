package com.pavelshapel.aws.spring.boot.starter.impl.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.pavelshapel.aop.spring.boot.starter.AopStarterAutoConfiguration;
import com.pavelshapel.aws.spring.boot.starter.AwsStarterAutoConfiguration;
import com.pavelshapel.aws.spring.boot.starter.api.service.RequestHandler;
import com.pavelshapel.json.spring.boot.starter.JsonStarterAutoConfiguration;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@SpringBootTest(classes = {
        AwsStarterAutoConfiguration.class,
        JsonStarterAutoConfiguration.class,
        AnnotationAwareAspectJAutoProxyCreator.class,
        AopStarterAutoConfiguration.class
})
@FieldDefaults(level = AccessLevel.PRIVATE)
class ApiGatewayRequestHandlerTest {
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