package com.pavelshapel.json.spring.boot.starter.converter.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavelshapel.core.spring.boot.starter.api.util.ExceptionUtils;
import com.pavelshapel.json.spring.boot.starter.converter.JsonConverter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
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
public class JacksonJsonConverter implements JsonConverter {
    ObjectMapper customObjectMapper;
    ExceptionUtils exceptionUtils;

    @Override
    public <P> String pojoToJson(P pojo) {
        try {
            return Optional.of(customObjectMapper.writeValueAsString(pojo))
                    .filter(this::isValidJson)
                    .orElseThrow();
        } catch (Exception exception) {
            throw createIllegalArgumentExceptionWithPojo(pojo);
        }
    }

    @Override
    public <P> P jsonToPojo(String json, Class<P> targetClass) {
        try {
            return customObjectMapper.readValue(json, targetClass);
        } catch (Exception exception) {
            throw exceptionUtils.createIllegalArgumentException(
                    JSON, json,
                    TARGET_CLASS, targetClass);
        }
    }

    @Override
    public <P> P inputStreamToPojo(InputStream inputStream, Class<P> targetClass) {
        try {
            return customObjectMapper.readValue(inputStream, targetClass);
        } catch (Exception exception) {
            throw exceptionUtils.createIllegalArgumentException(
                    INPUT_STREAM, inputStream,
                    TARGET_CLASS, targetClass);
        }
    }

    @Override
    public <P> List<P> inputStreamToPojos(InputStream inputStream, Class<P[]> targetClasses) {
        try {
            return Arrays.asList(customObjectMapper.readValue(inputStream, targetClasses));
        } catch (Exception exception) {
            throw exceptionUtils.createIllegalArgumentException(
                    INPUT_STREAM, inputStream,
                    TARGET_CLASSES, targetClasses);
        }
    }

    @Override
    public <P, M> Map<String, M> pojoToMap(P pojo) {
        try {
            return Optional.ofNullable(customObjectMapper.convertValue(pojo, new TypeReference<Map<String, M>>() {
            })).orElseThrow(() -> createIllegalArgumentExceptionWithPojo(pojo));
        } catch (Exception exception) {
            throw createIllegalArgumentExceptionWithPojo(pojo);
        }
    }

    @Override
    public <P, M> P mapToPojo(Map<String, M> map, Class<P> targetClass) {
        try {
            return Optional.ofNullable(customObjectMapper.convertValue(map, targetClass))
                    .orElseThrow();
        } catch (Exception exception) {
            throw exceptionUtils.createIllegalArgumentException(
                    MAP, map,
                    TARGET_CLASS, targetClass);
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
    public <P> String pojoToPrettyJson(P pojo) {
        try {
            return customObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(pojo);
        } catch (Exception exception) {
            throw createIllegalArgumentExceptionWithPojo(pojo);
        }
    }

    @Override
    public String getNodeAsString(String json, String... nodes) {
        try {
            return Optional.of(customObjectMapper.readTree(json))
                    .map(jsonNode -> getNode(jsonNode, nodes, 0))
                    .map(JsonNode::textValue)
                    .orElseThrow();
        } catch (Exception exception) {
            throw exceptionUtils.createIllegalArgumentException(
                    JSON, json,
                    NODES, nodes);
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

    private <P> RuntimeException createIllegalArgumentExceptionWithPojo(P pojo) {
        return exceptionUtils.createIllegalArgumentException(
                POJO, pojo
        );
    }
}