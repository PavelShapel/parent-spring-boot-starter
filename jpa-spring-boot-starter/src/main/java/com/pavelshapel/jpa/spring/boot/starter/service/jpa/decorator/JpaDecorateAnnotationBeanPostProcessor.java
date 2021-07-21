package com.pavelshapel.jpa.spring.boot.starter.service.jpa.decorator;

import com.pavelshapel.jpa.spring.boot.starter.service.jpa.JpaService;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class JpaDecorateAnnotationBeanPostProcessor implements BeanPostProcessor {
    @Autowired
    private ApplicationContext context;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof JpaService && !(bean instanceof AbstractDecoratorJpaService)) {
            bean = getDecoratedBean(bean);
        }
        return bean;
    }

    private Object getDecoratedBean(Object wrapped) {
        JpaDecorate annotation = getAnnotation(wrapped);
        if (Objects.isNull(annotation)) {
            return wrapped;
        }
        return iterateDecorationsInAnnotation(wrapped, annotation);
    }

    private JpaDecorate getAnnotation(Object wrapped) {
        Class<?> targetClass = AopUtils.getTargetClass(wrapped);
        return targetClass.getAnnotation(JpaDecorate.class);
    }

    private Object iterateDecorationsInAnnotation(Object wrapped, JpaDecorate annotation) {
        for (Class<? extends JpaService<?>> decorationClass : annotation.decorations()) {
            JpaService<?> decoratingBean = getDecoratingBean(decorationClass);
            wrapped = getWrappedWithDecoration(decoratingBean, wrapped);
        }
        return wrapped;
    }

    private JpaService<?> getDecoratingBean(Class<?> decorationClass) {
        return (JpaService<?>) context.getBean(decorationClass);
    }

    private JpaService<?> getWrappedWithDecoration(JpaService<?> decoratingBean, Object wrapped) {
        AbstractDecoratorJpaService<?> decorator = (AbstractDecoratorJpaService<?>) decoratingBean;
        decorator.setWrapped((JpaService) wrapped);
        return decorator;
    }
}