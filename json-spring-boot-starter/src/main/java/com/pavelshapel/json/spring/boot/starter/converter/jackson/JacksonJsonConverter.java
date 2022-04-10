package com.pavelshapel.json.spring.boot.starter.converter.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
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
            return Optional.of(objectMapper.writeValueAsString(object))
                    .filter(this::isValidJson);
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    @Override
    public <T> Optional<T> jsonToPojo(String json, Class<T> targetClass) {
        try {
            return Optional.of(objectMapper.readValue(json, targetClass));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Map<String, Object>> pojoToMap(Object object) {
        try {
            return Optional.of(objectMapper.convertValue(object, new TypeReference<Map<String, Object>>() {
            }));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    @Override
    public <T> Optional<T> mapToPojo(Map<String, Object> map, Class<T> targetClass) {
        try {
            return Optional.of(objectMapper.convertValue(map, targetClass));
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

    @Override
    public Optional<String> pojoToPrettyJson(Object object) {
        try {
            return Optional.of(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> getNodeAsString(String json, String... nodes) {
        try {
            return Optional.of(objectMapper.readTree(json))
                    .map(jsonNode -> getNode(jsonNode, nodes, 0))
                    .map(JsonNode::textValue);
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    private JsonNode getNode(JsonNode root, String[] nodes, int index) {
        return Optional.of(index)
                .filter(i -> i < nodes.length)
                .map(i -> nodes[i])
                .filter(root::hasNonNull)
                .map(root::get)
                .map(jsonNode -> getNode(jsonNode, nodes, index + 1))
                .orElse(root);
    }
}