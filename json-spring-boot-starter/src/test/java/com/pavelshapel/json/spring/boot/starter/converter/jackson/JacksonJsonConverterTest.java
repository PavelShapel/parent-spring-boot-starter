package com.pavelshapel.json.spring.boot.starter.converter.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pavelshapel.core.spring.boot.starter.CoreStarterAutoConfiguration;
import com.pavelshapel.core.spring.boot.starter.api.util.ExceptionUtils;
import com.pavelshapel.json.spring.boot.starter.JsonStarterAutoConfiguration;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(classes = {
        JsonStarterAutoConfiguration.class,
        CoreStarterAutoConfiguration.class
})
@FieldDefaults(level = AccessLevel.PRIVATE)
class JacksonJsonConverterTest {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final Integer ID_VALUE = 1;
    private static final String NAME_VALUE = NAME;
    private static final String JSON_POJO = String.format("{\"%s\":%d,\"%s\":\"%s\"}", ID, ID_VALUE, NAME, NAME_VALUE);
    public static final String SOURCE_JSON = "source.json";

    @TempDir
    Path tempDir;
    @SpyBean
    ObjectMapper customObjectMapper;
    @MockBean
    ExceptionUtils exceptionUtils;
    @Autowired
    JacksonJsonConverter jsonConverter;

    @Test
    void pojoToJson_ValidPojoAsParam_ShouldReturnJson() {
        JsonTester pojo = createTestPojo();

        String result = jsonConverter.pojoToJson(pojo);

        assertThat(result).isEqualTo(JSON_POJO);
    }

    @ParameterizedTest
    @NullSource
    void pojoToJson_NullAsParam_ShouldThrowException(JsonTester pojo) {
        mockExceptionUtilsCreateIllegalArgumentException();

        assertThatThrownBy(() -> jsonConverter.pojoToJson(pojo))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void pojoToJson_InvalidPojoAsParam_ShouldThrowException() {
        String pojo = getInvalidJson();
        mockExceptionUtilsCreateIllegalArgumentException();

        assertThatThrownBy(() -> jsonConverter.pojoToJson(pojo))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void jsonToPojo_ValidJsonAsParam_ShouldReturnPojo() {
        JsonTester pojo = createTestPojo();

        JsonTester result = jsonConverter.jsonToPojo(JSON_POJO, JsonTester.class);

        assertThat(result).isEqualTo(pojo);
    }

    @Test
    void jsonToPojo_InvalidStringAsParam_ShouldThrowException() {
        String json = getInvalidJson();
        mockExceptionUtilsCreateIllegalArgumentException();

        assertThatThrownBy(() -> jsonConverter.jsonToPojo(json, JsonTester.class))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @NullSource
    void jsonToPojo_NullStringAsParam_ShouldThrowException(String json) {
        mockExceptionUtilsCreateIllegalArgumentException();

        assertThatThrownBy(() -> jsonConverter.jsonToPojo(json, JsonTester.class))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void jsonToPojo_NullClassAsParam_ShouldThrowException() {
        mockExceptionUtilsCreateIllegalArgumentException();

        assertThatThrownBy(() -> jsonConverter.jsonToPojo(JSON_POJO, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void pojoToMap_ValidPojoAsParam_ShouldReturnMap() {
        JsonTester pojo = createTestPojo();
        Map<String, Object> map = createTestMap();

        Map<String, JsonTester> result = jsonConverter.pojoToMap(pojo);

        assertThat(result).isEqualTo(map);
    }

    @ParameterizedTest
    @NullSource
    void pojoToMap_NullAsParam_ShouldThrowException(JsonTester pojo) {
        mockExceptionUtilsCreateIllegalArgumentException();

        assertThatThrownBy(() -> jsonConverter.pojoToMap(pojo))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void pojoToMap_InvalidPojoAsParam_ShouldThrowException() {
        String pojo = getInvalidJson();
        mockExceptionUtilsCreateIllegalArgumentException();

        assertThatThrownBy(() -> jsonConverter.pojoToMap(pojo))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void mapToPojo_ValidMapAsParam_ShouldReturnPojo() {
        Map<String, Object> map = createTestMap();
        JsonTester pojo = createTestPojo();

        JsonTester result = jsonConverter.mapToPojo(map, JsonTester.class);

        assertThat(result).isEqualTo(pojo);
    }

    @ParameterizedTest
    @NullSource
    void mapToPojo_NullMapAsParam_ShouldThrowException(Map<String, Object> map) {
        mockExceptionUtilsCreateIllegalArgumentException();

        assertThatThrownBy(() -> jsonConverter.mapToPojo(map, JsonTester.class))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void mapToPojo_NullClassAsParam_ShouldThrowException() {
        Map<String, Object> map = createTestMap();
        mockExceptionUtilsCreateIllegalArgumentException();

        assertThatThrownBy(() -> jsonConverter.mapToPojo(map, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void isValidJson_ValidParam_ShouldReturnTrue() {
        boolean isValidJson = jsonConverter.isValidJson(JSON_POJO);

        assertThat(isValidJson).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"null", NAME_VALUE})
    void isValidJson_InvalidParam_ShouldReturnFalse(String json) {
        boolean isValidJson = jsonConverter.isValidJson(json);

        assertThat(isValidJson).isFalse();
    }

    @Test
    void pojoToPrettyJson_ValidPojoAsParam_ShouldReturnJson() {
        JsonTester pojo = createTestPojo();

        String json = jsonConverter.pojoToPrettyJson(pojo);

        assertThat(json).isNotEmpty();
    }

    @Test
    void getNodeAsString_WithValidParams_ShouldReturnResult() {
        String node = jsonConverter.getNodeAsString(JSON_POJO, NAME_VALUE);

        assertThat(node).isEqualTo(NAME_VALUE);
    }

    @Test
    void getNodeAsString_WithInValidParams_ShouldThrowException() {
        String json = getInvalidJson();
        mockExceptionUtilsCreateIllegalArgumentException();

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
    void inputStreamToPojo_NullClassAsParam_ShouldThrowException() {
        Path templatePath = tempDir.resolve(SOURCE_JSON);
        Files.write(templatePath, singleton(JSON_POJO));
        try (InputStream inputStream = Files.newInputStream(templatePath)) {
            mockExceptionUtilsCreateIllegalArgumentException();

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
    void inputStreamToPojos_NullClassAsParam_ShouldThrowException() {
        JsonTester pojo = createTestPojo();
        Path templatePath = tempDir.resolve(SOURCE_JSON);
        Files.write(templatePath, singleton(jsonConverter.pojoToJson(asList(pojo, pojo))));

        try (InputStream inputStream = Files.newInputStream(templatePath)) {
            mockExceptionUtilsCreateIllegalArgumentException();

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

    @SuppressWarnings("ThrowableNotThrown")
    private void mockExceptionUtilsCreateIllegalArgumentException() {
        doReturn(new IllegalArgumentException()).when(exceptionUtils).createIllegalArgumentException(any());
    }
}