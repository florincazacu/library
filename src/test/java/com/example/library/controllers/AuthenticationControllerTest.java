package com.example.library.controllers;

import com.example.library.BaseTest;
import com.example.library.model.security.UserDto;
import com.example.library.repositories.security.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AuthenticationControllerTest extends BaseTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void login() throws Exception {
        UserDto user = new UserDto();
        user.setUsername("user");
        user.setPassword("password");
        String json = new ObjectMapper().writeValueAsString(user);

        MvcResult mvcResult = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jwt = mvcResult.getResponse().getContentAsString();

        assertTrue(StringUtils.isNotBlank(jwt));
    }

    @Test
    public void loginBadCredentials() throws Exception {
        UserDto user = new UserDto();
        user.setUsername("badUser");
        user.setPassword("password");
        String json = new ObjectMapper().writeValueAsString(user);

        MvcResult mvcResult = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();

        String jwt = mvcResult.getResponse().getContentAsString();

        assertTrue(StringUtils.isBlank(jwt));
    }

    @Test
    public void register() throws Exception {
        UserDto user = new UserDto();
        user.setUsername("dummyUser");
        user.setPassword("password");
        user.setEmail("user@domain.com");
        String json = new ObjectMapper().writeValueAsString(user);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void registerWrongEmail() throws Exception {
        UserDto user = new UserDto();
        user.setUsername("dummyUser");
        user.setPassword("password");
        user.setEmail("user@domain");
        String json = new ObjectMapper().writeValueAsString(user);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void logout() throws Exception {

        UserDto user = new UserDto();
        user.setUsername("user1");
        user.setPassword("password");
        String json = new ObjectMapper().writeValueAsString(user);

        MvcResult mvcResult = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jwt = mvcResult.getResponse().getContentAsString();

        mockMvc.perform(delete("/logout")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNoContent());
    }
}