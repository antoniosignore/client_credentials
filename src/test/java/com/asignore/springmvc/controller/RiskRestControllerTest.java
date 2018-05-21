package com.asignore.springmvc.controller;


import com.asignore.springmvc.configuration.RootConfiguration;
import com.asignore.springmvc.dto.AuthDTO;
import com.asignore.springmvc.dto.ValueDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfiguration.class)
@WebAppConfiguration
public class RiskRestControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void should_receive_unauthorized_status() throws Exception {

        // exexute the GET and expect an unauthorized response
        // curl -X GET -H "Accept: application/json" http://localhost:8080/test/api/v1.0/risk
        ObjectMapper mapper = new ObjectMapper();

        ValueDTO valueDTO = new ValueDTO();
        valueDTO.setValue(1);

        mvc.perform(post("/api/v1.0/risk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(valueDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Accept", "application/json"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void should_receive_authorized_status_after_retrieving__access_token() throws Exception {

        //    curl -X POST -H "Accept: application/json" -H "Authorization: Basic Y29kaW5nX3Rlc3Q6YndabTVYQzZIVGxyM2ZjZHpSbkQ="  http://localhost:8080/test/oauth/token?grant_type=client_credentials
        MvcResult resultActions = mvc.perform(post("/oauth/token?grant_type=client_credentials")
                .header("Accept", "application/json")
                .header("Authorization", "Basic Y29kaW5nX3Rlc3Q6YndabTVYQzZIVGxyM2ZjZHpSbkQ="))
                .andExpect(status().isOk()).andReturn();

        // deserialize the returned object
        String content = resultActions.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        AuthDTO bean = mapper.readValue(content, AuthDTO.class);

        // access the risk GET endpoint with the Bearer token

        ValueDTO valueDTO = new ValueDTO();
        valueDTO.setValue(1);
        mvc.perform(post("/api/v1.0/risk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(valueDTO))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + bean.getAccessToken()))
                .andExpect(status().isOk());
    }
}