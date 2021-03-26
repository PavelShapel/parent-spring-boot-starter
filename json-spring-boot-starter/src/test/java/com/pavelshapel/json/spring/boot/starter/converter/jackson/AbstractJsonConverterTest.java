package com.pavelshapel.json.spring.boot.starter.converter.jackson;

import com.pavelshapel.json.spring.boot.starter.converter.JsonConverter;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

abstract class AbstractJsonConverterTest {
    private static final Integer ID = 1;
    private static final String NAME = "name";
    private static final String JSON_TESTER = String.format("{\"id\":%d,\"name\":\"%s\"}", ID, NAME);

    private final JsonConverter jsonConverter;

    protected AbstractJsonConverterTest(JsonConverter jsonConverter) {
        this.jsonConverter = jsonConverter;
    }

    @Test
    void pojoToJson_ValidPojoAsParam_ShouldReturnJson() {
        Optional<String> optionalJson = jsonConverter.pojoToJson(createTesterPojo());
        final String json = optionalJson.orElseThrow(IllegalArgumentException::new);


        assertThat(json).isEqualTo(JSON_TESTER);
    }

    @Test
    void pojoToJson_NullAsParam_ShouldReturnOptionalEmpty() {
        final Optional<String> optionalJson = jsonConverter.pojoToJson(null);


        assertThat(optionalJson).isEmpty();
    }

    @Test
    void pojoToJson_InvalidPojoAsParam_ShouldReturnOptionalEmpty() {
        final Optional<String> optionalJson =
                jsonConverter.pojoToJson(getInvalidJson());


        assertThat(optionalJson).isEmpty();
    }

    @Test
    void jsonToPojo_ValidJsonAsParam_ShouldReturnPojo() {
        Optional<JsonTester> optionalTester = jsonConverter.jsonToPojo(JSON_TESTER, JsonTester.class);
        final JsonTester jsonTester = optionalTester.orElseThrow(IllegalArgumentException::new);


        assertThat(jsonTester).isEqualTo(createTesterPojo());
    }

    @Test
    void jsonToPojo_InvalidStringAsParam_ShouldReturnOptionalEmpty() {
        final Optional<JsonTester> optionalTester = jsonConverter.jsonToPojo(getInvalidJson(), JsonTester.class);


        AssertionsForClassTypes.assertThat(optionalTester).isEmpty();
    }

    @Test
    void jsonToPojo_NullStringAsParam_ShouldReturnOptionalEmpty() {
        final Optional<JsonTester> optionalTester = jsonConverter.jsonToPojo(null, JsonTester.class);


        AssertionsForClassTypes.assertThat(optionalTester).isEmpty();
    }

    @Test
    void jsonToPojo_NullClassAsParam_ShouldReturnOptionalEmpty() {
        final Optional<JsonTester> optionalTester = jsonConverter.jsonToPojo(JSON_TESTER, null);


        AssertionsForClassTypes.assertThat(optionalTester).isEmpty();
    }

    @Test
    void pojoToMap_ValidPojoAsParam_ShouldReturnMap() {
        JsonTester jsonTester = createTesterPojo();
        Optional<Map<String, Object>> optionalMap = jsonConverter.pojoToMap(jsonTester);
        final Map<String, Object> map = optionalMap.orElseThrow(IllegalArgumentException::new);


        assertThat(map.get("id")).isSameAs(jsonTester.getId());
        assertThat(map.get("name")).isSameAs(jsonTester.getName());
    }

    @Test
    void pojoToMap_NullAsParam_ShouldReturnOptionalEmpty() {
        Optional<Map<String, Object>> optionalMap = jsonConverter.pojoToMap(null);


        assertThat(optionalMap).isEmpty();
    }

    @Test
    void pojoToMap_InvalidPojoAsParam_ShouldReturnOptionalEmpty() {
        Optional<Map<String, Object>> optionalMap = jsonConverter.pojoToMap(getInvalidJson());


        assertThat(optionalMap).isEmpty();
    }

    @Test
    void mapToPojo_ValidMapAsParam_ShouldReturnPojo() {
        Map<String, Object> map = createTesterMap();


        Optional<JsonTester> optionalTester = jsonConverter.mapToPojo(map, JsonTester.class);
        final JsonTester jsonTester = optionalTester.orElseThrow(IllegalArgumentException::new);


        assertThat(jsonTester.getId()).isEqualTo(map.get("id"));
        assertThat(jsonTester.getName()).isEqualTo(map.get("name"));
    }

    @Test
    void mapToPojo_NullMapAsParam_ShouldReturnPojo() {
        Optional<JsonTester> optionalTester = jsonConverter.mapToPojo(null, JsonTester.class);


        AssertionsForClassTypes.assertThat(optionalTester).isEmpty();
    }

    @Test
    void mapToPojo_NullClassAsParam_ShouldReturnPojo() {
        Optional<JsonTester> optionalTester = jsonConverter.mapToPojo(createTesterMap(), null);


        AssertionsForClassTypes.assertThat(optionalTester).isEmpty();
    }

    @Test
    void isValidJson_ValidParam_ShouldReturnTrue() {
        final boolean isValidJson = jsonConverter.isValidJson(JSON_TESTER);


        assertThat(isValidJson).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"null", NAME})
    void isValidJson_InvalidParam_ShouldReturnFalse(String json) {
        final boolean isValidJson = jsonConverter.isValidJson(json);


        assertThat(isValidJson).isFalse();
    }


    private String getInvalidJson() {
        return "invalidJson";
    }

    private JsonTester createTesterPojo() {
        return new JsonTester(ID, NAME);
    }

    private Map<String, Object> createTesterMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", ID);
        map.put("name", NAME);
        return map;
    }
}