package com.pavelshapel.aws.spring.boot.starter.api.service;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.http.HttpMethod;

import java.util.List;

public interface ResponseHandler {
    APIGatewayV2HTTPResponse updateResponseWithOkAndGet(APIGatewayV2HTTPResponse response, String responseBody);

    APIGatewayV2HTTPResponse updateResponseWithOkAndGet(APIGatewayV2HTTPResponse response, S3Object s3Object);

    APIGatewayV2HTTPResponse updateResponseWithBadRequestAndGet(APIGatewayV2HTTPResponse response, Exception exception);

    APIGatewayV2HTTPResponse updateResponseWithBadRequestAndGet(APIGatewayV2HTTPResponse response, List<HttpMethod> httpMethods);
}
