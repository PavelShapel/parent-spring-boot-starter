//package com.pavelshapel.aop.spring.boot.starter.log.method;
//
//import javafx.util.Pair;
//import lombok.extern.log4j.Log4j2;
//import org.apache.logging.log4j.Level;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//
//import java.lang.reflect.Method;
//import java.lang.reflect.Proxy;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Log4j2
//public class LoggableAnnotationBeanPostProcessor implements BeanPostProcessor {
//    private static final String LOG_PATTERN = "[{}.{}] {} -> {}";
//    private static final String NOTHING_TO_LOG = "nothing to log";
//    private final Map<String, Pair<Class<?>, List<Method>>> loggableBeans = new HashMap<>();
//
//    @Override
//    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        storeLoggableBeans(bean, beanName);
//        return bean;
//    }
//
//    private void storeLoggableBeans(Object bean, String beanName) {
//        Class<?> beanClass = bean.getClass();
//        List<Method> loggableMethods = getLoggableMethods(beanClass);
//        if (loggableMethods.isEmpty()) {
//            storeLoggableClass(beanName, beanClass);
//        } else {
//            storeLoggableMethods(beanName, beanClass, loggableMethods);
//        }
//    }
//
//    private void storeLoggableClass(String beanName, Class<?> beanClass) {
//        Optional.ofNullable(beanClass.getAnnotation(Loggable.class))
//                .ifPresent(loggable -> storeLoggableMethods(beanName, beanClass, Collections.emptyList()));
//    }
//
//    private void storeLoggableMethods(String beanName, Class<?> beanClass, List<Method> loggableMethods) {
//        loggableBeans.put(beanName, new Pair<>(beanClass, loggableMethods));
//    }
//
//    private List<Method> getLoggableMethods(Class<?> beanClass) {
//        return Arrays.stream(beanClass.getMethods())
//                .filter(method -> method.isAnnotationPresent(Loggable.class))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        return loggableBeans.containsKey(beanName)
//                ? createProxy(bean, beanName)
//                : bean;
//    }
//
//    private Object createProxy(Object bean, String beanName) {
//        Class<?> beanClass = bean.getClass();
//        return Proxy.newProxyInstance(beanClass.getClassLoader(), beanClass.getInterfaces(), (proxy, method, args) -> {
//            long startTimeMillis = System.currentTimeMillis();
//            Object result = method.invoke(bean, args);
//            long duration = System.currentTimeMillis() - startTimeMillis;
//            LoggableMethodSpecification loggableMethodSpecification = new LoggableMethodSpecification(method);
//            if (isContainingLoggableType(loggableMethodSpecification, LoggableType.METHOD_DURATION)) {
//                logDuration(loggableMethodSpecification, duration);
//            }
//            return result;
//        });
//    }
//
////    private void logSuccess(LoggableMethodSpecification loggableMethodSpecification, Object result) {
////        if (isResponseEntityErrorNotLogged(loggableMethodSpecification, result)) {
////            Level level = Level.toLevel(loggableMethodSpecification.getLoggable().level().toString());
////            log.log(level,
////                    LOG_PATTERN,
////                    loggableMethodSpecification.getMethodDeclaringClassName(),
////                    loggableMethodSpecification.getMethodName(),
////                    LoggableType.METHOD_RESULT.getPrefix(),
////                    getVerifiedLogResult(result)
////            );
////        }
////    }
//
////    private boolean isResponseEntityErrorNotLogged(LoggableMethodSpecification loggableMethodSpecification, Object result) {
////        if (result instanceof ResponseEntity) {
////            final ResponseEntity<?> responseEntity = (ResponseEntity<?>) result;
////            if (responseEntity.getStatusCode().isError()) {
////                log.error(LOG_PATTERN,
////                        loggableMethodSpecification.getMethodDeclaringClassName(),
////                        loggableMethodSpecification.getMethodName(),
////                        LoggableType.METHOD_EXCEPTION.getPrefix(),
////                        getVerifiedLogResult(responseEntity));
////                return false;
////            }
////        }
////        return true;
////    }
//
//    private void logException(LoggableMethodSpecification loggableMethodSpecification, Throwable throwable) {
//        log.error(
//                LOG_PATTERN,
//                loggableMethodSpecification.getMethodDeclaringClassName(),
//                loggableMethodSpecification.getMethodName(),
//                LoggableType.METHOD_EXCEPTION.getPrefix(),
//                getVerifiedLogResult(throwable)
//        );
//    }
//
//    private void logDuration(LoggableMethodSpecification loggableMethodSpecification, long duration) {
//        Level level = Level.toLevel(loggableMethodSpecification.getLoggable().level().toString());
//        log.log(level,
//                LOG_PATTERN,
//                loggableMethodSpecification.getMethodDeclaringClassName(),
//                loggableMethodSpecification.getMethodName(),
//                LoggableType.METHOD_DURATION.getPrefix(),
//                getVerifiedLogResult(String.format("%d ms", duration))
//        );
//    }
//
//    private boolean isContainingLoggableType(LoggableMethodSpecification loggableMethodSpecification, LoggableType loggableType) {
//        return Arrays.asList(loggableMethodSpecification.getLoggable().value())
//                .contains(loggableType);
//    }
//
//    private String getVerifiedLogResult(Object result) {
//        return Objects.isNull(result) || result.toString().isEmpty() ? NOTHING_TO_LOG : result.toString();
//    }
//}
