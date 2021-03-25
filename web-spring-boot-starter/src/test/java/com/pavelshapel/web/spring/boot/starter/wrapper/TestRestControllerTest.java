package com.pavelshapel.web.spring.boot.starter.wrapper;

import com.pavelshapel.web.spring.boot.starter.StarterAutoConfiguration;
import com.pavelshapel.web.spring.boot.starter.wrapper.controller.TestRestController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.stream.Stream;

import static com.pavelshapel.web.spring.boot.starter.wrapper.controller.TestTypes.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@WebMvcTest
@ContextConfiguration(classes = {
        StarterAutoConfiguration.class,
        TestRestController.class
})
class TestRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void initialization() {
        assertThat(mockMvc).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("generateTypes")
    void test_TestRestController_ShouldReturnHttpServletRequest(String testTypes) throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/{testTypes}", testTypes))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        int status = mvcResult.getResponse().getStatus();

        assertThat(responseBody).isNotNull();
        assertThat(status).isEqualTo(HttpStatus.OK.value());
    }

    private static Stream<Arguments> generateTypes() {
        return Stream.of(
                arguments(STRING.name()),
                arguments(BOOLEAN.name()),
                arguments(LONG.name()),
                arguments(DATE.name()),
                arguments(DOUBLE.name())
        );
    }
}