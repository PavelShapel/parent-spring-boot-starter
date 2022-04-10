package com.pavelshapel.json.spring.boot.starter.converter.jackson;

import com.pavelshapel.json.spring.boot.starter.converter.JsonConverter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
    void initialization() {
        assertThat(jsonConverter).isNotNull();
    }

    @Test
    void pojoToJson_ValidPojoAsParam_ShouldReturnJson() {
        Optional<String> optionalJson = jsonConverter.pojoToJson(createTestPojo());

        assertThat(optionalJson)
                .isNotEmpty()
                .hasValue(JSON_POJO);
    }

    @Test
    void pojoToJson_NullAsParam_ShouldReturnOptionalEmpty() {
        Optional<String> optionalJson = jsonConverter.pojoToJson(null);

        assertThat(optionalJson)
                .isEmpty();
    }

    @Test
    void pojoToJson_InvalidPojoAsParam_ShouldReturnOptionalEmpty() {
        Optional<String> optionalJson = jsonConverter.pojoToJson(getInvalidJson());

        assertThat(optionalJson)
                .isEmpty();
    }

    @Test
    void jsonToPojo_ValidJsonAsParam_ShouldReturnPojo() {
        Optional<JsonTester> optionalTester = jsonConverter.jsonToPojo(JSON_POJO, JsonTester.class);

        JsonTester jsonTester = createTestPojo();
        assertThat(optionalTester)
                .isNotEmpty()
                .hasValue(jsonTester);
    }

    @Test
    void jsonToPojo_InvalidStringAsParam_ShouldReturnOptionalEmpty() {
        Optional<JsonTester> optionalTester = jsonConverter.jsonToPojo(getInvalidJson(), JsonTester.class);

        assertThat(optionalTester)
                .isEmpty();
    }

    @Test
    void jsonToPojo_NullStringAsParam_ShouldReturnOptionalEmpty() {
        Optional<JsonTester> optionalTester = jsonConverter.jsonToPojo(null, JsonTester.class);

        assertThat(optionalTester)
                .isEmpty();
    }

    @Test
    void jsonToPojo_NullClassAsParam_ShouldReturnOptionalEmpty() {
        Optional<JsonTester> optionalTester = jsonConverter.jsonToPojo(JSON_POJO, null);

        assertThat(optionalTester)
                .isEmpty();
    }

    @Test
    void pojoToMap_ValidPojoAsParam_ShouldReturnMap() {
        Optional<Map<String, Object>> optionalMap = jsonConverter.pojoToMap(createTestPojo());

        assertThat(optionalMap)
                .isNotEmpty()
                .hasValue(createTestMap());
    }

    @Test
    void pojoToMap_NullAsParam_ShouldReturnOptionalEmpty() {
        Optional<Map<String, Object>> optionalMap = jsonConverter.pojoToMap(null);

        assertThat(optionalMap)
                .isEmpty();
    }

    @Test
    void pojoToMap_InvalidPojoAsParam_ShouldReturnOptionalEmpty() {
        Optional<Map<String, Object>> optionalMap = jsonConverter.pojoToMap(getInvalidJson());

        assertThat(optionalMap)
                .isEmpty();
    }

    @Test
    void mapToPojo_ValidMapAsParam_ShouldReturnPojo() {
        Optional<JsonTester> optionalTester = jsonConverter.mapToPojo(createTestMap(), JsonTester.class);

        JsonTester jsonTester = createTestPojo();
        assertThat(optionalTester)
                .isNotEmpty()
                .hasValue(jsonTester);
    }

    @Test
    void mapToPojo_NullMapAsParam_ShouldReturnPojo() {
        Optional<JsonTester> optionalTester = jsonConverter.mapToPojo(null, JsonTester.class);

        assertThat(optionalTester)
                .isEmpty();
    }

    @Test
    void mapToPojo_NullClassAsParam_ShouldReturnPojo() {
        Optional<JsonTester> optionalTester = jsonConverter.mapToPojo(createTestMap(), null);

        assertThat(optionalTester)
                .isEmpty();
    }

    @Test
    void isValidJson_ValidParam_ShouldReturnTrue() {
        final boolean isValidJson = jsonConverter.isValidJson(JSON_POJO);

        assertThat(isValidJson)
                .isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"null", NAME_VALUE})
    void isValidJson_InvalidParam_ShouldReturnFalse(String json) {
        boolean isValidJson = jsonConverter.isValidJson(json);

        assertThat(isValidJson)
                .isFalse();
    }

    @Test
    void pojoToPrettyJson_ValidPojoAsParam_ShouldReturnJson() {
        Optional<String> optionalJson = jsonConverter.pojoToPrettyJson(createTestPojo());

        assertThat(optionalJson)
                .isNotEmpty();
    }

    @Test
    void getNodeAsString_WithValidParams_ShouldReturnResult() {
        Optional<String> optionalNode = jsonConverter.getNodeAsString(JSON_POJO, NAME_VALUE);

        assertThat(optionalNode)
                .isNotEmpty()
                .hasValue(NAME_VALUE);
    }

    @Test
    void getNodeAsString_WithInValidParams_ShouldReturnOptionalEmpty() {
        Optional<String> optionalNode = jsonConverter.getNodeAsString(getInvalidJson(), NAME_VALUE);

        assertThat(optionalNode)
                .isEmpty();
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