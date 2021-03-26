package com.pavelshapel.web.spring.boot.starter.wrapper;

import com.pavelshapel.json.spring.boot.starter.converter.JsonConverter;
import com.pavelshapel.json.spring.boot.starter.converter.jackson.JacksonJsonConverter;
import com.pavelshapel.web.spring.boot.starter.StarterAutoConfiguration;
import com.pavelshapel.web.spring.boot.starter.wrapper.controller.TestTypedResponseWrapperRestController;
import com.pavelshapel.web.spring.boot.starter.wrapper.provider.TestTypesProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@WebMvcTest
@ContextConfiguration(classes = {
        StarterAutoConfiguration.class,
        TestTypedResponseWrapperRestController.class,
        JacksonJsonConverter.class
})
class TestTypedResponseWrapperRestControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JsonConverter jsonConverter;

    @Test
    void initialization() {
        assertThat(mockMvc).isNotNull();
        assertThat(jsonConverter).isNotNull();
    }

    @ParameterizedTest
    @ArgumentsSource(TestTypesProvider.class)
    void test_TestRestController_ShouldReturnHttpServletRequest(String testTypes) throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/{testTypes}", testTypes))
                .andReturn();


        String responseBody = mvcResult.getResponse().getContentAsString();
        int status = mvcResult.getResponse().getStatus();


        assertThat(responseBody).isNotNull();
        assertThat(status).isEqualTo(HttpStatus.OK.value());

        final Optional<TypedResponseWrapper> typedResponseWrapper = jsonConverter.jsonToPojo(responseBody, TypedResponseWrapper.class);
        typedResponseWrapper.ifPresent(value ->
                assertThat(value.getType()).isEqualToIgnoringCase(testTypes));
    }
}