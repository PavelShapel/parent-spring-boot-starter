package com.pavelshapel.kafka.spring.boot.starter.aop;

import com.pavelshapel.kafka.spring.boot.starter.service.KafkaProducer;
import com.pavelshapel.web.spring.boot.starter.web.converter.AbstractDto;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class KafkaSenderAnnotationBeanPostProcessor implements BeanPostProcessor {
    final Map<String, List<Method>> kafkaSenderBeans = new HashMap<>();
    @Autowired
    KafkaProducer kafkaProducer;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        List<Method> methods = Arrays.stream(bean.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(KafkaSender.class))
                .collect(Collectors.toList());
        if (!methods.isEmpty()) {
            kafkaSenderBeans.put(beanName, methods);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        List<Method> kafkaSenderMethods = kafkaSenderBeans.get(beanName);
        if (nonNull(kafkaSenderMethods)) {
            Class<?> beanClass = bean.getClass();
            return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), getInvocationHandler(bean, kafkaSenderMethods));
        }
        return bean;
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
        if (result instanceof AbstractDto) {
            KafkaSender annotation = method.getAnnotation(KafkaSender.class);
            kafkaProducer.send(annotation.topic(), (AbstractDto) result);
        }
    }
}