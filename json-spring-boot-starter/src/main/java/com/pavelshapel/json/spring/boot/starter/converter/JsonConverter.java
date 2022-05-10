package com.pavelshapel.json.spring.boot.starter.converter;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface JsonConverter {
    <P> String pojoToJson(P pojo);

    <P> P jsonToPojo(String json, Class<P> targetClass);

    <P> P inputStreamToPojo(InputStream inputStream, Class<P> targetClass);

    <P> List<P> inputStreamToPojos(InputStream inputStream, Class<P[]> targetClass);

    <P, M> Map<String, M> pojoToMap(P pojo);

    <P, M> P mapToPojo(Map<String, M> map, Class<P> targetClass);

    boolean isValidJson(String json);

    <P> String pojoToPrettyJson(P pojo);

    String getNodeAsString(String json, String... nodes);
}