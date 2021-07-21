package com.pavelshapel.json.spring.boot.starter.converter;

import java.util.Map;
import java.util.Optional;

public interface JsonConverter {
    Optional<String> pojoToJson(Object object);

    <T> Optional<T> jsonToPojo(String json, Class<? extends T> targetClass);

    Optional<Map<String, Object>> pojoToMap(Object object);

    <T> Optional<T> mapToPojo(Map<String, Object> map, Class<? extends T> targetClass);

    boolean isValidJson(String json);

    Optional<String> pojoToPrettyJson(Object object);
}