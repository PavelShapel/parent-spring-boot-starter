package com.pavelshapel.starter.boot.spring.json;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

final class JacksonJsonConverter implements JsonConverter {
  private final ObjectMapper objectMapper;

  JacksonJsonConverter(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public <P> String pojoToJson(P pojo) {
    return objectMapper.writeValueAsString(pojo);
  }

  @Override
  public <P> P jsonToPojo(String json, Class<P> targetClass) {
    return objectMapper.readValue(json, targetClass);
  }

  @Override
  public <P> P base64ToPojo(String base64, Class<P> targetClass) {
    String decodedJson = new String(Base64.getDecoder().decode(base64));
    return jsonToPojo(decodedJson, targetClass);
  }

  @Override
  public <P> String pojoToBase64(P pojo) {
    String json = pojoToJson(pojo);
    return Base64.getEncoder().encodeToString(json.getBytes());
  }

  @Override
  public <P> P inputStreamToPojo(InputStream inputStream, Class<P> targetClass) {
    return objectMapper.readValue(inputStream, targetClass);
  }

  @Override
  public <P> List<P> inputStreamToPojos(InputStream inputStream, Class<P[]> targetClasses) {
    return Arrays.asList(objectMapper.readValue(inputStream, targetClasses));
  }

  @Override
  public <P, M> Map<String, M> pojoToMap(P pojo) {
    return objectMapper.convertValue(pojo, new TypeReference<>() {});
  }

  @Override
  public <P, M> P mapToPojo(Map<String, M> map, Class<P> targetClass) {
    return objectMapper.convertValue(map, targetClass);
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
  public String getNodeAsString(String json, String... nodes) {
    return Optional.of(objectMapper.readTree(json))
        .map(jsonNode -> getNode(jsonNode, nodes, /* index= */ 0))
        .map(JsonNode::stringValue)
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
