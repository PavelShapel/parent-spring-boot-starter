package com.pavelshapel.json.spring.boot.starter.converter.jackson;

import com.pavelshapel.json.spring.boot.starter.StarterAutoConfiguration;
import com.pavelshapel.json.spring.boot.starter.converter.JsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static com.pavelshapel.json.spring.boot.starter.StarterAutoConfiguration.*;

@SpringBootTest(properties = {
        PREFIX + "." + PROPERTY_NAME + "=" + TRUE
})
@ContextConfiguration(classes = {
        StarterAutoConfiguration.class
})
class JacksonJsonConverterTest extends AbstractJsonConverterTest {

    @Autowired
    protected JacksonJsonConverterTest(JsonConverter jacksonJsonConverter) {
        super(jacksonJsonConverter);
    }
}