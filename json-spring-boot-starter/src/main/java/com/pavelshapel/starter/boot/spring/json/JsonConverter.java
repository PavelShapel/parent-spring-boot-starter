package com.pavelshapel.starter.boot.spring.json;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface JsonConverter {
  <P> String pojoToJson(P pojo);

  <P> P jsonToPojo(String json, Class<P> targetClass);

  <P> P base64ToPojo(String base64, Class<P> targetClass);

  <P> String pojoToBase64(P pojo);

  <P> P inputStreamToPojo(InputStream inputStream, Class<P> targetClass);

  <P> List<P> inputStreamToPojos(InputStream inputStream, Class<P[]> targetClass);

  <P, M> Map<String, M> pojoToMap(P pojo);

  <P, M> P mapToPojo(Map<String, M> map, Class<P> targetClass);

  boolean isValidJson(String json);

  String getNodeAsString(String json, String... nodes);
}
