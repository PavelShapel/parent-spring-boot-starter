package com.pavelshapel.json.spring.boot.starter.converter.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavelshapel.json.spring.boot.starter.converter.JsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.Map;
import java.util.Optional;

public class JacksonJsonConverter implements JsonConverter {
    private final ObjectMapper objectMapper;

    @Autowired
    public JacksonJsonConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<String> pojoToJson(Object object) {
        try {
            final String json = objectMapper.writeValueAsString(object);
            return isValidJson(json) ? Optional.ofNullable(json) : Optional.empty();
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    @Override
    public <T> Optional<T> jsonToPojo(String json, Class<? extends T> targetClass) {
        try {
            return isValidJson(json) ? Optional.ofNullable(objectMapper.readValue(json, targetClass)) : Optional.empty();
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Map<String, Object>> pojoToMap(Object object) {
        try {
            return Optional.ofNullable(objectMapper.convertValue(object, new TypeReference<Map<String, Object>>() {
            }));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    @Override
    public <T> Optional<T> mapToPojo(Map<String, Object> map, Class<? extends T> targetClass) {
        try {
            return Optional.ofNullable(objectMapper.convertValue(map, targetClass));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    @Override
    public boolean isValidJson(String json) {
        try {
            new JSONObject(json);
        } catch (Exception externalException) {
            try {
                new JSONArray(json);
            } catch (Exception internalException) {
                return false;
            }
        }
        return true;
    }
}