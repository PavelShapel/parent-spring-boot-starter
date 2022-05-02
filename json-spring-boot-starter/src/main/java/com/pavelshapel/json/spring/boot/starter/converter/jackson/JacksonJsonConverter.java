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
    public <P> String pojoToJson(P pojo) {
        try {
            String json = objectMapper.writeValueAsString(pojo);
            return Optional.ofNullable(json)
                    .filter(this::isValidJson)
                    .orElseThrow(() -> new JsonConverterException(buildPojoMessage(pojo), buildJsonMessage(json)));
        } catch (Exception exception) {
            throw new JsonConverterException(exception, buildPojoMessage(pojo));
        }
    }

    @Override
    public <P> P jsonToPojo(String json, Class<P> targetClass) {
        try {
            return objectMapper.readValue(json, targetClass);
        } catch (Exception exception) {
            throw new JsonConverterException(exception, buildJsonMessage(json), buildClassMessage(targetClass));
        }
    }

    @Override
    public <P, M> Map<String, M> pojoToMap(P pojo) {
        try {
            return Optional.ofNullable(objectMapper.convertValue(pojo, new TypeReference<Map<String, M>>() {
            })).orElseThrow(() -> new JsonConverterException(buildPojoMessage(pojo)));
        } catch (Exception exception) {
            throw new JsonConverterException(exception, buildPojoMessage(pojo));
        }
    }

    @Override
    public <P, M> P mapToPojo(Map<String, M> map, Class<P> targetClass) {
        try {
            return Optional.ofNullable(objectMapper.convertValue(map, targetClass))
                    .orElseThrow(() -> new JsonConverterException(buildMapMessage(map), buildClassMessage(targetClass)));
        } catch (Exception exception) {
            throw new JsonConverterException(exception, buildMapMessage(map), buildClassMessage(targetClass));
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
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(pojo);
        } catch (Exception exception) {
            throw new JsonConverterException(exception, buildPojoMessage(pojo));
        }
    }

    @Override
    public String getNodeAsString(String json, String... nodes) {
        try {
            return Optional.ofNullable(objectMapper.readTree(json))
                    .map(jsonNode -> getNode(jsonNode, nodes, 0))
                    .map(JsonNode::textValue)
                    .orElseThrow(() -> new JsonConverterException(buildJsonMessage(json), buildNodesMessage(String.join(",", nodes))));
        } catch (Exception exception) {
            throw new JsonConverterException(exception, buildJsonMessage(json), buildNodesMessage(String.join(",", nodes)));
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

    private String buildPojoMessage(Object object) {
        return buildArgumentPattern("pojo", object);
    }

    private String buildJsonMessage(Object object) {
        return buildArgumentPattern("json", object);
    }

    private String buildMapMessage(Object object) {
        return buildArgumentPattern("map", object);
    }

    private String buildClassMessage(Object object) {
        return buildArgumentPattern("class", object);
    }

    private String buildNodesMessage(Object object) {
        return buildArgumentPattern("nodes", object);
    }

    private String buildArgumentPattern(String argument, Object object) {
        return String.format("%s = [%s]", argument, object);
    }
}