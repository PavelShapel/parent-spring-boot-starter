package com.pavelshapel.jpa.spring.boot.starter.service.jpa.decorator;

import com.pavelshapel.jpa.spring.boot.starter.service.jpa.JpaService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class JpaDecorateAnnotationBeanPostProcessor implements BeanPostProcessor {
    @Autowired
    private ApplicationContext context;

    private final Map<String, Class<?>> jpaDecorateBeans = new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        boolean isJpaService = bean instanceof JpaService;
        boolean isNotDecoratorJpaService = !(bean instanceof AbstractDecoratorJpaService);
        boolean isJpaDecoratePresent = beanClass.isAnnotationPresent(JpaDecorate.class);
        if (isJpaService && isNotDecoratorJpaService && isJpaDecoratePresent) {
            jpaDecorateBeans.put(beanName, beanClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return jpaDecorateBeans.containsKey(beanName)
                ? getDecoratedBean(bean, beanName)
                : bean;
    }

    private Object getDecoratedBean(Object bean, String beanName) {
        return iterateDecorationsInAnnotation(bean, beanName);
    }

    private Object iterateDecorationsInAnnotation(Object wrapped, String wrappedName) {
        JpaDecorate annotation = jpaDecorateBeans.get(wrappedName).getAnnotation(JpaDecorate.class);
        for (Class<? extends JpaService<?>> decorationClass : annotation.decorations()) {
            JpaService<?> wrapper = getWrapper(decorationClass);
            wrapped = getWrappedWithDecoration(wrapper, wrappedName);
        }
        return wrapped;
    }

    private JpaService<?> getWrapper(Class<?> decorationClass) {
        return (JpaService<?>) context.getBean(decorationClass);
    }

    private Object getWrappedWithDecoration(JpaService<?> decoratingBean, String wrappedName) {
        AbstractDecoratorJpaService<?> decorator = (AbstractDecoratorJpaService<?>) decoratingBean;
        decorator.setWrapped((JpaService) context.getBean(wrappedName));
        return decorator;
    }
}