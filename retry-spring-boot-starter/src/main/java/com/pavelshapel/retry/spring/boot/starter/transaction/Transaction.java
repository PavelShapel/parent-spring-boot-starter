package com.pavelshapel.retry.spring.boot.starter.transaction;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class Transaction {
    private final Runnable task;
    private final String description;
    @Setter(AccessLevel.PROTECTED)
    private boolean isSucceed;

    @Override
    public String toString() {
        return description;
    }
}
