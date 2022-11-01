package com.pavelshapel.json.spring.boot.starter.converter.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavelshapel.aop.spring.boot.starter.annotation.ExceptionWrapped;
import com.pavelshapel.json.spring.boot.starter.converter.JsonConverter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@ExceptionWrapped
public class JacksonJsonConverter implements JsonConverter {
    ObjectMapper customObjectMapper;

    @Override
    @SneakyThrows
    public <P> String pojoToJson(P pojo) {
        return Optional.of(customObjectMapper.writeValueAsString(pojo))
                .filter(this::isValidJson)
                .orElseThrow();
    }

    @Override
    @SneakyThrows
    public <P> P jsonToPojo(String json, Class<P> targetClass) {
        return customObjectMapper.readValue(json, targetClass);
    }

    @Override
    @SneakyThrows
    public <P> P inputStreamToPojo(InputStream inputStream, Class<P> targetClass) {
        return customObjectMapper.readValue(inputStream, targetClass);
    }

    @Override
    @SneakyThrows
    public <P> List<P> inputStreamToPojos(InputStream inputStream, Class<P[]> targetClasses) {
        return Arrays.asList(customObjectMapper.readValue(inputStream, targetClasses));
    }

    @Override
    @SneakyThrows
    public <P, M> Map<String, M> pojoToMap(P pojo) {
        return Optional.ofNullable(customObjectMapper.convertValue(pojo, new TypeReference<Map<String, M>>() {
        })).orElseThrow();
    }

    @Override
    @SneakyThrows
    public <P, M> P mapToPojo(Map<String, M> map, Class<P> targetClass) {
        return Optional.ofNullable(customObjectMapper.convertValue(map, targetClass))
                .orElseThrow();
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
    @SneakyThrows
    public <P> String pojoToPrettyJson(P pojo) {
        return customObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(pojo);
    }

    @Override
    @SneakyThrows
    public String getNodeAsString(String json, String... nodes) {
        return Optional.of(customObjectMapper.readTree(json))
                .map(jsonNode -> getNode(jsonNode, nodes, 0))
                .map(JsonNode::textValue)
                .orElseThrow();
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