package com.pavelshapel.webflux.spring.boot.starter.api;

import java.util.Optional;

public interface ApiService<P, R> {
    Optional<R> get(P parameters);
}