package com.pavelshapel.json.spring.boot.starter.converter.jackson;

import com.pavelshapel.json.spring.boot.starter.JsonStarterAutoConfiguration;
import com.pavelshapel.json.spring.boot.starter.converter.JsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {
        JsonStarterAutoConfiguration.class
})
class JacksonJsonConverterTest extends AbstractJsonConverterTest {

    @Autowired
    protected JacksonJsonConverterTest(JsonConverter jacksonJsonConverter) {
        super(jacksonJsonConverter);
    }
}