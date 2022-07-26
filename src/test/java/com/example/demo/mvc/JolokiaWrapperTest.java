package com.example.demo.mvc;

import com.example.demo.config.WebMvcConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringJUnitWebConfig(WebMvcConfig.class)
class JolokiaWrapperTest {

    MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
    }

    @Test
    public void testHelloController() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    public void testJolokiaChecksAuth() throws Exception {
        this.mockMvc.perform(get("/jolokia"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = {"SCOPE_jolokia_access"})
    public void testJolokiaAuthorized() throws Exception {
        this.mockMvc.perform(get("/jolokia"))
                .andExpect(status().isOk());
    }
}