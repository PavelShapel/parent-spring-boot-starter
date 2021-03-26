package com.pavelshapel.web.spring.boot.starter.wrapper;

import com.pavelshapel.web.spring.boot.starter.StarterAutoConfiguration;
import com.pavelshapel.web.spring.boot.starter.wrapper.controller.TestRestController;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
    @ArgumentsSource(TestTypesProvider.class)
    void test_TestRestController_ShouldReturnNotWrappedRequest(String testTypes) throws Exception {
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/{testTypes}", testTypes))
                .andReturn();


        String responseBody = mvcResult.getResponse().getContentAsString();
        int status = mvcResult.getResponse().getStatus();


        assertThat(responseBody).isNotNull();
        assertThat(status).isEqualTo(HttpStatus.OK.value());
    }
}