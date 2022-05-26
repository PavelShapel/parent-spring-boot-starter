package com.pavelshapel.core.spring.boot.starter.impl.service.decorator;

import com.pavelshapel.core.spring.boot.starter.impl.annotation.AutowiredLazy;
import com.pavelshapel.core.spring.boot.starter.api.service.DaoService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.StringUtils.uncapitalize;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class DecorateDaoServiceAnnotationBeanPostProcessor implements BeanPostProcessor {
    final Map<String, Class<?>> daoDecorateBeans = new HashMap<>();

    @AutowiredLazy
    ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        boolean isDaoService = bean instanceof DaoService;
        boolean isNotAbstractDecoratorDaoService = !(bean instanceof AbstractDecoratorDaoService);
        boolean isDaoDecoratePresent = beanClass.isAnnotationPresent(DecorateDaoService.class);
        if (isDaoService && isNotAbstractDecoratorDaoService && isDaoDecoratePresent) {
            daoDecorateBeans.put(beanName, beanClass);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        return daoDecorateBeans.containsKey(beanName)
                ? getDecoratedBean(bean, beanName)
                : bean;
    }

    private Object getDecoratedBean(Object bean, String beanName) {
        return iterateDecorationsInAnnotation(bean, beanName);
    }

    private Object iterateDecorationsInAnnotation(Object wrapped, String wrappedName) {
        DecorateDaoService annotation = daoDecorateBeans.get(wrappedName).getAnnotation(DecorateDaoService.class);
        for (Class<? extends DaoService<?, ?>> decorationClass : annotation.decorations()) {
            String decorationBeanName = uncapitalize(decorationClass.getSimpleName());
            DaoService<?, ?> wrapper = getWrapper(decorationBeanName);
            wrapped = getWrappedWithDecoration(wrapper, wrapped);
        }
        return wrapped;
    }

    private DaoService<?, ?> getWrapper(String beanName) {
        return (DaoService<?, ?>) applicationContext.getBean(beanName);
    }

    private Object getWrappedWithDecoration(Object wrapper, Object wrapped) {
        AbstractDecoratorDaoService<?, ?> decorator = (AbstractDecoratorDaoService<?, ?>) wrapper;
        decorator.setWrapped((DaoService) wrapped);
        return decorator;
    }
}