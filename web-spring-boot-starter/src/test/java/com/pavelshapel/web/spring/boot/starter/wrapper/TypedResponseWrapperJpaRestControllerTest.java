package com.pavelshapel.web.spring.boot.starter.wrapper;

import com.pavelshapel.web.spring.boot.starter.WebStarterAutoConfiguration;
import com.pavelshapel.web.spring.boot.starter.wrapper.controller.TestTypedResponseWrapperRestController;
import com.pavelshapel.web.spring.boot.starter.wrapper.provider.TypesProvider;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = {
        WebStarterAutoConfiguration.class,
        TestTypedResponseWrapperRestController.class
})
class TypedResponseWrapperJpaRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @EnumSource(TypesProvider.class)
    void test_TestRestController_ShouldReturnWrappedRequest(TypesProvider typesProvider) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/{testTypes}", typesProvider.name()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}