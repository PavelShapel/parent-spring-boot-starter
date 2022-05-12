package com.pavelshapel.webflux.spring.boot.starter.api;

public interface ApiService<P, R> {
    R get(P parameters);
}