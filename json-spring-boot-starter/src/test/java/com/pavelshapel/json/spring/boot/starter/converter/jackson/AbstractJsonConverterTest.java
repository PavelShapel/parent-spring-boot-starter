package com.pavelshapel.json.spring.boot.starter.converter.jackson;

import com.pavelshapel.json.spring.boot.starter.converter.JsonConverter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

abstract class AbstractJsonConverterTest {
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final Integer ID_VALUE = 1;
    private static final String NAME_VALUE = NAME;
    private static final String JSON_POJO = String.format("{\"%s\":%d,\"%s\":\"%s\"}", ID, ID_VALUE, NAME, NAME_VALUE);

    private final JsonConverter jsonConverter;

    protected AbstractJsonConverterTest(JsonConverter jsonConverter) {
        this.jsonConverter = jsonConverter;
    }

    @Test
    void pojoToJson_ValidPojoAsParam_ShouldReturnJson() {
        JsonTester pojo = createTestPojo();

        String json = jsonConverter.pojoToJson(pojo);

        assertThat(json).isEqualTo(JSON_POJO);
    }

    @Test
    void pojoToJson_NullAsParam_ShouldThrowException() {
        JsonTester pojo = null;

        assertThatThrownBy(() -> jsonConverter.pojoToJson(pojo))
                .isInstanceOf(JsonConverterException.class);
    }

    @Test
    void pojoToJson_InvalidPojoAsParam_ShouldThrowException() {
        String pojo = getInvalidJson();

        assertThatThrownBy(() -> jsonConverter.pojoToJson(pojo))
                .isInstanceOf(JsonConverterException.class);
    }

    @Test
    void jsonToPojo_ValidJsonAsParam_ShouldReturnPojo() {
        JsonTester jsonTester = jsonConverter.jsonToPojo(JSON_POJO, JsonTester.class);

        assertThat(jsonTester).isEqualTo(createTestPojo());
    }

    @Test
    void jsonToPojo_InvalidStringAsParam_ShouldThrowException() {
        String json = getInvalidJson();

        assertThatThrownBy(() -> jsonConverter.jsonToPojo(json, JsonTester.class))
                .isInstanceOf(JsonConverterException.class);
    }

    @Test
    void jsonToPojo_NullStringAsParam_ShouldThrowException() {
        String json = null;

        assertThatThrownBy(() -> jsonConverter.jsonToPojo(json, JsonTester.class))
                .isInstanceOf(JsonConverterException.class);
    }

    @Test
    void jsonToPojo_NullClassAsParam_ShouldThrowException() {
        assertThatThrownBy(() -> jsonConverter.jsonToPojo(JSON_POJO, null))
                .isInstanceOf(JsonConverterException.class);
    }

    @Test
    void pojoToMap_ValidPojoAsParam_ShouldReturnMap() {
        JsonTester pojo = createTestPojo();

        Map<String, JsonTester> map = jsonConverter.pojoToMap(pojo);

        assertThat(map).isEqualTo(createTestMap());
    }

    @Test
    void pojoToMap_NullAsParam_ShouldThrowException() {
        JsonTester pojo = null;

        assertThatThrownBy(() -> jsonConverter.pojoToMap(pojo))
                .isInstanceOf(JsonConverterException.class);
    }

    @Test
    void pojoToMap_InvalidPojoAsParam_ShouldThrowException() {
        String pojo = getInvalidJson();

        assertThatThrownBy(() -> jsonConverter.pojoToMap(pojo))
                .isInstanceOf(JsonConverterException.class);
    }

    @Test
    void mapToPojo_ValidMapAsParam_ShouldReturnPojo() {
        Map<String, Object> map = createTestMap();

        JsonTester jsonTester = jsonConverter.mapToPojo(map, JsonTester.class);

        assertThat(jsonTester).isEqualTo(createTestPojo());
    }

    @Test
    void mapToPojo_NullMapAsParam_ShouldThrowException() {
        Map<String, Object> map = null;

        assertThatThrownBy(() -> jsonConverter.mapToPojo(map, JsonTester.class))
                .isInstanceOf(JsonConverterException.class);
    }

    @Test
    void mapToPojo_NullClassAsParam_ShouldThrowException() {
        Map<String, Object> map = createTestMap();

        assertThatThrownBy(() -> jsonConverter.mapToPojo(map, null))
                .isInstanceOf(JsonConverterException.class);
    }

    @Test
    void isValidJson_ValidParam_ShouldReturnTrue() {
        final boolean isValidJson = jsonConverter.isValidJson(JSON_POJO);

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
        String json = jsonConverter.pojoToPrettyJson(createTestPojo());

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

        assertThatThrownBy(() -> jsonConverter.getNodeAsString(json, NAME_VALUE))
                .isInstanceOf(JsonConverterException.class);
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