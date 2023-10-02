package com.pavelshapel.retry.spring.boot.starter;

import com.pavelshapel.retry.spring.boot.starter.transaction.TransactionExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
public class RetryStarterAutoConfiguration {
    public static final String TYPE = "retry";

    @Bean
    public RetryContextRefreshedListener retryContextRefreshedListener() {
        return new RetryContextRefreshedListener();
    }

    @Bean
    public TransactionExecutor transactionExecutor() {
        return new TransactionExecutor();
    }
}