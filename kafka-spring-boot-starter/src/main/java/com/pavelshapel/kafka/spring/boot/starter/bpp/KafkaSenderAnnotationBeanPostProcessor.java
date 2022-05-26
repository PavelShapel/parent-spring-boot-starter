package com.pavelshapel.kafka.spring.boot.starter.bpp;

import com.pavelshapel.core.spring.boot.starter.api.model.Dto;
import com.pavelshapel.kafka.spring.boot.starter.service.KafkaProducer;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class KafkaSenderAnnotationBeanPostProcessor implements BeanPostProcessor {
    final Map<String, List<Method>> kafkaSenderBeans = new HashMap<>();
    @Autowired
    @Lazy
    KafkaProducer<Dto<String>> kafkaProducer;

    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        List<Method> methods = Arrays.stream(bean.getClass().getDeclaredMethods())
                .filter(this::isAnnotationKafkaSenderPresent)
                .collect(Collectors.toList());
        if (!methods.isEmpty()) {
            kafkaSenderBeans.put(beanName, methods);
        }
        return bean;
    }

    private boolean isAnnotationKafkaSenderPresent(Method method) {
        return method.isAnnotationPresent(KafkaSender.class);
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) {
        return Optional.ofNullable(kafkaSenderBeans.get(beanName))
                .map(methods -> createProxyInstance(bean, methods))
                .orElse(bean);
    }

    private Object createProxyInstance(Object bean, List<Method> methods) {
        Class<?> beanClass = bean.getClass();
        return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), getInvocationHandler(bean, methods));
    }

    private InvocationHandler getInvocationHandler(Object bean, List<Method> kafkaSenderMethods) {
        return (proxy, method, args) -> {
            Object result = method.invoke(bean, args);
            if (kafkaSenderMethods.contains(method)) {
                sendMessage(method, result);
            }
            return result;
        };
    }

    private void sendMessage(Method method, Object result) {
        KafkaSender annotation = method.getAnnotation(KafkaSender.class);
        Optional.ofNullable(result)
                .filter(Dto.class::isInstance)
                .map(Dto.class::cast)
                .ifPresent(dto -> kafkaProducer.send(annotation.topic(), annotation.key(), dto));
    }
}