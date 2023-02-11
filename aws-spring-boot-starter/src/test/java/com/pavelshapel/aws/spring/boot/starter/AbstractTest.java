package com.pavelshapel.aws.spring.boot.starter;

import com.pavelshapel.aop.spring.boot.starter.AopStarterAutoConfiguration;
import com.pavelshapel.aws.spring.boot.starter.config.DynamoDbAwsTestConfiguration;
import com.pavelshapel.aws.spring.boot.starter.config.S3AwsTestConfiguration;
import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import com.pavelshapel.json.spring.boot.starter.JsonStarterAutoConfiguration;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest(classes = {
        AwsStarterAutoConfiguration.class,
        CoreStarterAutoConfiguration.class,
        JsonStarterAutoConfiguration.class,
        AnnotationAwareAspectJAutoProxyCreator.class,
        AopStarterAutoConfiguration.class,
        S3AwsTestConfiguration.class,
        DynamoDbAwsTestConfiguration.class
})
public abstract class AbstractTest {
    protected String createValidName(String name) {
        return Optional.ofNullable(name)
                .map(String::toLowerCase)
                .map("com-pavelshapel-"::concat)
                .map(this::getVerifiedName)
                .orElseThrow();
    }

    private String getVerifiedName(String name) {
        return name.length() > 63 ? name.substring(0, 63) : name;
    }

}
