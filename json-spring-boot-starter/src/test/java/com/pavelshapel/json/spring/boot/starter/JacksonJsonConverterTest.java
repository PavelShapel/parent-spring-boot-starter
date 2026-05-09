package com.pavelshapel.json.spring.boot.starter;

import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {JacksonAutoConfiguration.class, JsonStarterAutoConfiguration.class})
class JacksonJsonConverterTest {
  private static final String ID = "id";
  private static final String NAME = "name";
  private static final Integer ID_VALUE = 1;
  private static final String NAME_VALUE = "test";
  private static final String JSON_POJO =
      String.format("{\"%s\":%d,\"%s\":\"%s\"}", ID, ID_VALUE, NAME, NAME_VALUE);
  private static final JsonTester TEST_POJO = new JsonTester(ID_VALUE, NAME_VALUE);
  private static final String SOURCE_JSON = "source.json";
  private static final String BASE64_POJO =
      Base64.getEncoder().encodeToString(JSON_POJO.getBytes());

  private record JsonTester(Integer id, String name) {}

  @TempDir Path tempDir;

  @Autowired JsonConverter jsonConverter;

  @Test
  void shouldReturnJsonWhenConvertingValidPojoToJson() {
    String result = jsonConverter.pojoToJson(TEST_POJO);

    assertThat(result).isEqualTo(JSON_POJO);
  }

  @Test
  void shouldReturnPojoWhenConvertingValidJsonToPojo() {
    JsonTester result = jsonConverter.jsonToPojo(JSON_POJO, JsonTester.class);

    assertThat(result).isEqualTo(TEST_POJO);
  }

  @Test
  void shouldReturnMapWhenConvertingValidPojoToMap() {
    Map<String, Object> map = createTestMap();

    Map<String, JsonTester> result = jsonConverter.pojoToMap(TEST_POJO);

    assertThat(result).isEqualTo(map);
  }

  @Test
  void shouldReturnPojoWhenConvertingValidMapToPojo() {
    Map<String, Object> map = createTestMap();

    JsonTester result = jsonConverter.mapToPojo(map, JsonTester.class);

    assertThat(result).isEqualTo(TEST_POJO);
  }

  @Test
  void shouldReturnTrueWhenValidJsonIsChecked() {
    boolean result = jsonConverter.isValidJson(JSON_POJO);

    assertThat(result).isTrue();
  }

  @ParameterizedTest
  @ValueSource(strings = {"null", NAME_VALUE})
  void shouldReturnFalseWhenInvalidJsonIsChecked(String json) {
    boolean result = jsonConverter.isValidJson(json);

    assertThat(result).isFalse();
  }

  @Test
  void shouldReturnNodeAsStringWhenValidParamsProvided() {
    String node = jsonConverter.getNodeAsString(JSON_POJO, NAME);

    assertThat(node).isEqualTo(NAME_VALUE);
  }

  @Test
  void shouldReturnPojoWhenConvertingValidInputStreamToPojo() throws IOException {
    Path templatePath = tempDir.resolve(SOURCE_JSON);
    Files.write(templatePath, singleton(JSON_POJO));
    try (InputStream inputStream = Files.newInputStream(templatePath)) {
      JsonTester result = jsonConverter.inputStreamToPojo(inputStream, JsonTester.class);

      assertThat(result).isEqualTo(TEST_POJO);
    }
  }

  @Test
  void shouldReturnPojosWhenConvertingValidInputStreamToPojos() throws IOException {
    JsonTester pojo = TEST_POJO;
    Path templatePath = tempDir.resolve(SOURCE_JSON);
    List<JsonTester> pojos = List.of(pojo, pojo);
    Files.write(templatePath, singleton(jsonConverter.pojoToJson(pojos)));

    try (InputStream inputStream = Files.newInputStream(templatePath)) {
      List<JsonTester> result = jsonConverter.inputStreamToPojos(inputStream, JsonTester[].class);

      assertThat(result).hasSize(2).isEqualTo(pojos);
    }
  }

  @Test
  void shouldReturnBase64WhenConvertingValidPojoToBase64() {
    String result = jsonConverter.pojoToBase64(TEST_POJO);

    assertThat(result).isEqualTo(BASE64_POJO);
  }

  @Test
  void shouldReturnPojoWhenConvertingValidBase64ToPojo() {
    JsonTester result = jsonConverter.base64ToPojo(BASE64_POJO, JsonTester.class);

    assertThat(result).isEqualTo(TEST_POJO);
  }

  private Map<String, Object> createTestMap() {
    return Map.of(
        ID, ID_VALUE,
        NAME, NAME_VALUE);
  }
}
