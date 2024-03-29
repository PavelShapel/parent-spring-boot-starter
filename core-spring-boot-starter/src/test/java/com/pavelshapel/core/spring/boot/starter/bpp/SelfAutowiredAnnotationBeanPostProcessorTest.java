package com.pavelshapel.core.spring.boot.starter.bpp;

import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
        CoreStarterAutoConfiguration.class,
        SelfAutowiredTester.class
})
class SelfAutowiredAnnotationBeanPostProcessorTest {
    @Autowired
    private SelfAutowiredTester selfAutowiredTester;

    @Test
    void selfAutowired() {
        assertThat(selfAutowiredTester.getSelfAutowiredTester())
                .isNotNull();
    }
}