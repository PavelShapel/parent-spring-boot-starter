package com.pavelshapel.core.spring.boot.starter.reflection.annotation;

import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.Annotation;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ContextConfiguration(classes = {
        CoreStarterAutoConfiguration.class
})
class ClassAnnotationReplacerTest {
    public static final String MESSAGE = "message";
    public static final String NEW_MESSAGE = "new message";

    @Autowired
    private AnnotationReplacer annotationReplacer;

    @Test
    void replace_WithValidParams_ShouldReplaceAnnotation() {
        Replaced oldAnnotation = getReplacedAnnotation();
        Replaced newAnnotation = new Replaced() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return oldAnnotation.annotationType();
            }

            @Override
            public String value() {
                return NEW_MESSAGE;
            }
        };

        annotationReplacer.replace(TestPojo.class, Replaced.class, newAnnotation);

        assertThat(getReplacedAnnotation().value()).isEqualTo(NEW_MESSAGE);
    }

    private Replaced getReplacedAnnotation() {
        return TestPojo.class.getAnnotation(Replaced.class);
    }
}