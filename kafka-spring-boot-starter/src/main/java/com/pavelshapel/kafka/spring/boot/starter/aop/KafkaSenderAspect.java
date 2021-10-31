//package com.pavelshapel.kafka.spring.boot.starter.aop;
//
//import com.pavelshapel.json.spring.boot.starter.converter.JsonConverter;
//import lombok.AccessLevel;
//import lombok.experimental.FieldDefaults;
//import org.apache.commons.lang3.StringUtils;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.lang.reflect.Method;
//
//import static java.util.Objects.*;
//import static org.apache.commons.lang3.StringUtils.*;
//
//@Aspect
//@FieldDefaults(level = AccessLevel.PRIVATE)
//public class KafkaSenderAspect {
//    @Autowired
//    AspectKafkaService kafkaService;
//    @Autowired
//    JsonConverter jsonConverter;
//
//    @Pointcut("@annotation(KafkaSender)")
//    public void callKafkaSend() {
//        //callKafkaSend
//    }
//
//    @Around("callKafkaSend()")
//    public Object sendKafkaMessage(ProceedingJoinPoint joinPoint) throws Throwable {
//        Object result = joinPoint.proceed();
//        sendMessage(joinPoint, result);
//        return result;
//    }
//
//    private void sendMessage(ProceedingJoinPoint joinPoint, Object result) {
//        KafkaSender annotation = getKafkaSenderAnnotation(joinPoint);
//        String annotationValue = annotation.value();
//        String annotationKey = annotation.key();
//        String annotationTopic = annotation.topic();
//        if (isNoneEmpty(annotationValue)){
//            kafkaService.send(to)
//        }
//        if ((isNullOrEmpty(annotationValue) && isNullOrEmpty(result))) {
//            return;
//        }
//        String message = String.valueOf(result);
//
//
//        kafkaService.send(annotationTopic, annotationKey,
//                isEmpty(annotationValue) ? message : annotationValue);
//    }
//
//    private KafkaSender getKafkaSenderAnnotation(ProceedingJoinPoint joinPoint) {
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method method = methodSignature.getMethod();
//        return method.getAnnotation(KafkaSender.class);
//    }
//
//    private boolean isNullOrEmpty(Object obj) {
//        if (isNull(obj)) {
//            return true;
//        }
//        return (obj instanceof String) && ((String) obj).isEmpty();
//    }
//}