package com.pavelshapel.aws.spring.boot.starter;

import com.pavelshapel.aop.spring.boot.starter.AopStarterAutoConfiguration;
import com.pavelshapel.json.spring.boot.starter.JsonStarterAutoConfiguration;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {
        AwsStarterAutoConfiguration.class,
        JsonStarterAutoConfiguration.class,
        AnnotationAwareAspectJAutoProxyCreator.class,
        AopStarterAutoConfiguration.class,
        S3AwsTestConfiguration.class
})
public abstract class AbstractTest {
}
