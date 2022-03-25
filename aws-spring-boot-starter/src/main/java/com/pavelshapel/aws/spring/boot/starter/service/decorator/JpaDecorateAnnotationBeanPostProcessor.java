//package com.pavelshapel.aws.spring.boot.starter.service.decorator;
//
//import com.pavelshapel.aws.spring.boot.starter.service.DynamoDbService;
//import com.pavelshapel.jpa.spring.boot.starter.service.JpaService;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//import org.springframework.context.ApplicationContext;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static org.springframework.util.StringUtils.uncapitalize;
//
//public class JpaDecorateAnnotationBeanPostProcessor implements BeanPostProcessor {
//    private final Map<String, Class<?>> jpaDecorateBeans = new HashMap<>();
//
//    @Autowired
//    private ApplicationContext applicationContext;
//
//    @Override
//    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        Class<?> beanClass = bean.getClass();
//        boolean isDynamoDbServiceService = bean instanceof DynamoDbService;
//        boolean isNotDecoratorJpaService = !(bean instanceof AbstractDecoratorDynamoDbService);
//        boolean isJpaDecoratePresent = beanClass.isAnnotationPresent(DynamoDbDecorate.class);
//        if (isDynamoDbServiceService && isNotDecoratorJpaService && isJpaDecoratePresent) {
//            jpaDecorateBeans.put(beanName, beanClass);
//        }
//        return bean;
//    }
//
//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        return jpaDecorateBeans.containsKey(beanName)
//                ? getDecoratedBean(bean, beanName)
//                : bean;
//    }
//
//    private Object getDecoratedBean(Object bean, String beanName) {
//        return iterateDecorationsInAnnotation(bean, beanName);
//    }
//
//    private Object iterateDecorationsInAnnotation(Object wrapped, String wrappedName) {
//        DynamoDbDecorate annotation = jpaDecorateBeans.get(wrappedName).getAnnotation(DynamoDbDecorate.class);
//        for (Class<? extends JpaService<?, ?>> decorationClass : annotation.decorations()) {
//            String decorationBeanName = uncapitalize(decorationClass.getSimpleName());
//            JpaService<?, ?> wrapper = getWrapper(decorationBeanName);
//            wrapped = getWrappedWithDecoration(wrapper, wrapped);
//        }
//        return wrapped;
//    }
//
//    private JpaService<?, ?> getWrapper(String beanName) {
//        return (JpaService<?, ?>) applicationContext.getBean(beanName);
//    }
//
//    private Object getWrappedWithDecoration(Object wrapper, Object wrapped) {
//        AbstractDecoratorDynamoDbService<?, ?> decorator = (AbstractDecoratorDynamoDbService<?, ?>) wrapper;
//        decorator.setWrapped((JpaService) wrapped);
//        return decorator;
//    }
//}