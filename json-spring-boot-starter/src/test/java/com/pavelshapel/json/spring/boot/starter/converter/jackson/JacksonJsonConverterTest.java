package com.pavelshapel.json.spring.boot.starter.converter.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavelshapel.aop.spring.boot.starter.AopStarterAutoConfiguration;
import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import com.pavelshapel.json.spring.boot.starter.JsonStarterAutoConfiguration;
import com.pavelshapel.json.spring.boot.starter.converter.JsonConverter;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {
        JsonStarterAutoConfiguration.class,
        CoreStarterAutoConfiguration.class,
        AopStarterAutoConfiguration.class,
        AnnotationAwareAspectJAutoProxyCreator.class
})
@FieldDefaults(level = AccessLevel.PRIVATE)
class JacksonJsonConverterTest {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final Integer ID_VALUE = 1;
    private static final String NAME_VALUE = "test";
    private static final String JSON_POJO = String.format("{\"%s\":%d,\"%s\":\"%s\"}", ID, ID_VALUE, NAME, NAME_VALUE);
    public static final String SOURCE_JSON = "source.json";

    @TempDir
    Path tempDir;
    @SpyBean
    ObjectMapper customObjectMapper;
    @Autowired
    JsonConverter jsonConverter;

    @Test
    void pojoToJson_WithValidPojoAsParameter_ShouldReturnJson() {
        JsonTester pojo = createTestPojo();

        String result = jsonConverter.pojoToJson(pojo);

        assertThat(result).isEqualTo(JSON_POJO);
    }

    @ParameterizedTest
    @NullSource
    void pojoToJson_WithNullAsParameter_ShouldThrowException(JsonTester pojo) {
        assertThatThrownBy(() -> jsonConverter.pojoToJson(pojo))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void pojoToJson_WithInvalidPojoAsParameter_ShouldThrowException() {
        String pojo = getInvalidJson();

        assertThatThrownBy(() -> jsonConverter.pojoToJson(pojo))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void jsonToPojo_WithValidJsonAsParameter_ShouldReturnPojo() {
        JsonTester pojo = createTestPojo();

        JsonTester result = jsonConverter.jsonToPojo(JSON_POJO, JsonTester.class);

        assertThat(result).isEqualTo(pojo);
    }

    @Test
    void jsonToPojo_WithInvalidStringAsParameter_ShouldThrowException() {
        String json = getInvalidJson();

        assertThatThrownBy(() -> jsonConverter.jsonToPojo(json, JsonTester.class))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullSource
    void jsonToPojo_WithNullStringAsParameter_ShouldThrowException(String json) {
        assertThatThrownBy(() -> jsonConverter.jsonToPojo(json, JsonTester.class))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void jsonToPojo_WithNullClassAsParameter_ShouldThrowException() {
        assertThatThrownBy(() -> jsonConverter.jsonToPojo(JSON_POJO, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void pojoToMap_WithValidPojoAsParameter_ShouldReturnMap() {
        JsonTester pojo = createTestPojo();
        Map<String, Object> map = createTestMap();

        Map<String, JsonTester> result = jsonConverter.pojoToMap(pojo);

        assertThat(result).isEqualTo(map);
    }

    @ParameterizedTest
    @NullSource
    void pojoToMap_WithNullAsParameter_ShouldThrowException(JsonTester pojo) {
        assertThatThrownBy(() -> jsonConverter.pojoToMap(pojo))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void pojoToMap_WithInvalidPojoAsParameter_ShouldThrowException() {
        String pojo = getInvalidJson();

        assertThatThrownBy(() -> jsonConverter.pojoToMap(pojo))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void mapToPojo_WithValidMapAsParameter_ShouldReturnPojo() {
        Map<String, Object> map = createTestMap();
        JsonTester pojo = createTestPojo();

        JsonTester result = jsonConverter.mapToPojo(map, JsonTester.class);

        assertThat(result).isEqualTo(pojo);
    }

    @ParameterizedTest
    @NullSource
    void mapToPojo_WithNullMapAsParameter_ShouldThrowException(Map<String, Object> map) {

        assertThatThrownBy(() -> jsonConverter.mapToPojo(map, JsonTester.class))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void mapToPojo_WithNullClassAsParameter_ShouldThrowException() {
        Map<String, Object> map = createTestMap();

        assertThatThrownBy(() -> jsonConverter.mapToPojo(map, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void isValidJson_WithValidParam_ShouldReturnTrue() {
        boolean isValidJson = jsonConverter.isValidJson(JSON_POJO);

        assertThat(isValidJson).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"null", NAME_VALUE})
    void isValidJson_WithInvalidParam_ShouldReturnFalse(String json) {
        boolean isValidJson = jsonConverter.isValidJson(json);

        assertThat(isValidJson).isFalse();
    }

    @Test
    void pojoToPrettyJson_WithValidPojoAsParameter_ShouldReturnJson() {
        JsonTester pojo = createTestPojo();

        String json = jsonConverter.pojoToPrettyJson(pojo);

        assertThat(json).isNotEmpty();
    }

    @Test
    void getNodeAsString_WithValidParams_ShouldReturnResult() {
        String node = jsonConverter.getNodeAsString(JSON_POJO, NAME);

        assertThat(node).isEqualTo(NAME_VALUE);
    }

    @Test
    void getNodeAsString_WithInValidParams_ShouldThrowException() {
        String json = getInvalidJson();

        assertThatThrownBy(() -> jsonConverter.getNodeAsString(json, NAME_VALUE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @SneakyThrows
    @Test
    void inputStreamToPojo_WithValidParams_ShouldReturnResult() {
        Path templatePath = tempDir.resolve(SOURCE_JSON);
        Files.write(templatePath, singleton(JSON_POJO));
        JsonTester pojo = createTestPojo();
        try (InputStream inputStream = Files.newInputStream(templatePath)) {
            JsonTester result = jsonConverter.inputStreamToPojo(inputStream, JsonTester.class);

            assertThat(result).isEqualTo(pojo);
        }
    }

    @SneakyThrows
    @Test
    void inputStreamToPojo_WithNullClassAsParameter_ShouldThrowException() {
        Path templatePath = tempDir.resolve(SOURCE_JSON);
        Files.write(templatePath, singleton(JSON_POJO));
        try (InputStream inputStream = Files.newInputStream(templatePath)) {
            assertThatThrownBy(() -> jsonConverter.inputStreamToPojo(inputStream, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @SneakyThrows
    @Test
    void inputStreamToPojos_WithValidParams_ShouldReturnResult() {
        JsonTester pojo = createTestPojo();
        Path templatePath = tempDir.resolve(SOURCE_JSON);
        List<JsonTester> pojos = asList(pojo, pojo);
        Files.write(templatePath, singleton(jsonConverter.pojoToJson(pojos)));

        try (InputStream inputStream = Files.newInputStream(templatePath)) {
            List<JsonTester> result = jsonConverter.inputStreamToPojos(inputStream, JsonTester[].class);

            assertThat(result)
                    .asList()
                    .hasSize(2)
                    .isEqualTo(pojos);
        }
    }

    @SneakyThrows
    @Test
    void inputStreamToPojos_WithNullClassAsParameter_ShouldThrowException() {
        JsonTester pojo = createTestPojo();
        Path templatePath = tempDir.resolve(SOURCE_JSON);
        Files.write(templatePath, singleton(jsonConverter.pojoToJson(asList(pojo, pojo))));

        try (InputStream inputStream = Files.newInputStream(templatePath)) {
            assertThatThrownBy(() -> jsonConverter.inputStreamToPojos(inputStream, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private String getInvalidJson() {
        return RandomStringUtils.randomAlphanumeric(Byte.MAX_VALUE);
    }

    private JsonTester createTestPojo() {
        return new JsonTester(ID_VALUE, NAME_VALUE);
    }

    private Map<String, Object> createTestMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(ID, ID_VALUE);
        map.put(NAME, NAME_VALUE);
        return map;
    }
}