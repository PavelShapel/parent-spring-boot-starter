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
    private static final Integer ID = 1;
    private static final String NAME = "name";
    private static final String JSON_POJO = String.format("{\"id\":%d,\"name\":\"%s\"}", ID, NAME);

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
        Optional<TestPojo> optionalTester = jsonConverter.jsonToPojo(JSON_POJO, TestPojo.class);

        TestPojo testPojo = createTestPojo();
        assertThat(optionalTester)
                .isNotEmpty()
                .hasValue(testPojo);
    }

    @Test
    void jsonToPojo_InvalidStringAsParam_ShouldReturnOptionalEmpty() {
        Optional<TestPojo> optionalTester = jsonConverter.jsonToPojo(getInvalidJson(), TestPojo.class);

        assertThat(optionalTester)
                .isEmpty();
    }

    @Test
    void jsonToPojo_NullStringAsParam_ShouldReturnOptionalEmpty() {
        Optional<TestPojo> optionalTester = jsonConverter.jsonToPojo(null, TestPojo.class);

        assertThat(optionalTester)
                .isEmpty();
    }

    @Test
    void jsonToPojo_NullClassAsParam_ShouldReturnOptionalEmpty() {
        Optional<TestPojo> optionalTester = jsonConverter.jsonToPojo(JSON_POJO, null);

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
        Optional<TestPojo> optionalTester = jsonConverter.mapToPojo(createTestMap(), TestPojo.class);

        TestPojo testPojo = createTestPojo();
        assertThat(optionalTester)
                .isNotEmpty()
                .hasValue(testPojo);
    }

    @Test
    void mapToPojo_NullMapAsParam_ShouldReturnPojo() {
        Optional<TestPojo> optionalTester = jsonConverter.mapToPojo(null, TestPojo.class);

        assertThat(optionalTester)
                .isEmpty();
    }

    @Test
    void mapToPojo_NullClassAsParam_ShouldReturnPojo() {
        Optional<TestPojo> optionalTester = jsonConverter.mapToPojo(createTestMap(), null);

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
    @ValueSource(strings = {"null", NAME})
    void isValidJson_InvalidParam_ShouldReturnFalse(String json) {
        boolean isValidJson = jsonConverter.isValidJson(json);

        assertThat(isValidJson)
                .isFalse();
    }

    private String getInvalidJson() {
        return RandomStringUtils.randomAlphanumeric(Byte.MAX_VALUE);
    }

    private TestPojo createTestPojo() {
        return new TestPojo(ID, NAME);
    }

    private Map<String, Object> createTestMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", ID);
        map.put("name", NAME);
        return map;
    }
}