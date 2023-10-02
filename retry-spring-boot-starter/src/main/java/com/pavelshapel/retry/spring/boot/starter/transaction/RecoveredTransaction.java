package com.pavelshapel.retry.spring.boot.starter.transaction;

public record RecoveredTransaction(
        Transaction succeedTransaction,
        Transaction rollBackTransaction) {
}
