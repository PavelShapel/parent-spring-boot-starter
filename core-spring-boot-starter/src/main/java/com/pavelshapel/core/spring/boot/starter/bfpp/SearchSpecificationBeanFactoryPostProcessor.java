package com.pavelshapel.core.spring.boot.starter.bfpp;

import com.pavelshapel.core.spring.boot.starter.impl.web.search.AbstractSearchSpecification;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

public class SearchSpecificationBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        Arrays.stream(configurableListableBeanFactory.getBeanDefinitionNames())
                .map(configurableListableBeanFactory::getBeanDefinition)
                .filter(this::isAbstractSearchSpecification)
                .forEach(beanDefinition -> beanDefinition.setScope(SCOPE_PROTOTYPE));
    }

    private boolean isAbstractSearchSpecification(BeanDefinition beanDefinition) {
        return Optional.ofNullable(beanDefinition)
                .map(BeanDefinition::getBeanClassName)
                .map(this::getClassForName)
                .map(Class::getSuperclass)
                .filter(AbstractSearchSpecification.class::equals)
                .isPresent();
    }

    @SneakyThrows
    private Class<?> getClassForName(String className) {
        return Class.forName(className);
    }
}