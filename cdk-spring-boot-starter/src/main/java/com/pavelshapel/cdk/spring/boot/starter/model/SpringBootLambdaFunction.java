package com.pavelshapel.cdk.spring.boot.starter.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionUrl;
import software.amazon.awscdk.services.lambda.FunctionUrlAuthType;
import software.amazon.awscdk.services.lambda.FunctionUrlOptions;

import java.util.Optional;

import static software.amazon.awscdk.Duration.seconds;
import static software.amazon.awscdk.services.lambda.Code.fromAsset;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class SpringBootLambdaFunction {
    Function function;
    SpringBootLambdaProperties properties;

    public SpringBootLambdaFunction(SpringBootLambdaProperties properties) {
        this.properties = properties;
        this.function = Function.Builder.create(properties.getScope(), Function.class.getSimpleName())
                .runtime(properties.getRuntime())
                .code(fromAsset(properties.getAssetReference()))
                .handler(properties.getHandler())
                .memorySize(properties.getMemorySize())
                .timeout(seconds(properties.getTimeOut()))
                .reservedConcurrentExecutions(properties.getReservedConcurrentExecutions())
                .logRetention(properties.getRetention())
                .role(properties.getRole())
                .build();
    }

    public String addFunctionUrlWithNoneAuthType() {
        return Optional.of(FunctionUrlOptions.builder())
                .map(builder -> builder.authType(FunctionUrlAuthType.NONE))
                .map(FunctionUrlOptions.Builder::build)
                .map(function::addFunctionUrl)
                .map(this::addCfnOutput)
                .map(CfnOutput::getValue)
                .map(Object::toString)
                .orElseThrow();
    }

    private CfnOutput addCfnOutput(FunctionUrl functionUrl) {
        return CfnOutput.Builder.create(properties.getScope(), CfnOutput.class.getSimpleName())
                .value(functionUrl.getUrl())
                .build();
    }
}
