package com.sep.onlinedeliverysystem.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.controller.UserController;
import com.sep.onlinedeliverysystem.domain.entities.UserEntity;
import com.sep.onlinedeliverysystem.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerIntegrationTests {

    private UserService userService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public UserControllerIntegrationTests(MockMvc mockMvc, UserService userService) {
        this.userService = userService;
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateUserSuccessfullyReturnsHttp201Created() throws Exception {
        UserEntity testUser1 = TestUtil.userBuild1();
//        testUser1.setEmail(null); //should autogenerate for us... doesn't seem to as this causes test to fail
        String userJson = objectMapper.writeValueAsString(testUser1);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateUserSuccessfullyReturnsSavedUser() throws Exception {
        UserEntity testUser1 = TestUtil.userBuild1();
//        testUser1.setEmail(null); //should autogenerate for us... doesn't seem to as this causes test to fail
        String userJson = objectMapper.writeValueAsString(testUser1);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value("luke@trottmail.com")
        );
    }

    @Test
    public void testThatListUserSuccessfullyReturnsHttpStatus200Ok() throws Exception {
           mockMvc.perform(
                   MockMvcRequestBuilders.get("/users")
                           .contentType(MediaType.APPLICATION_JSON)
           ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatListUserSuccessfullyReturnsListOfUsers() throws Exception {
        UserEntity testUser1 = TestUtil.userBuild1();
        userService.createUser(testUser1);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].email").value("luke@trottmail.com")
        );
    }

    @Test
    public void testThatGetUserSuccessfullyReturnsHttpStatus200OkWhenUserExists() throws Exception {
        UserEntity testUser1 = TestUtil.userBuild1();
        userService.createUser(testUser1);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/luke@trottmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetUserSuccessfullyReturnsHttpStatus404NotFoundWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/incorrectemail")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatGetUserSuccessfullyReturnsUserWhenUserExists() throws Exception {
        UserEntity testUser1 = TestUtil.userBuild1();
        userService.createUser(testUser1);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/luke@trottmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value("luke@trottmail.com")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstName").value("Luke")
        );
    }

}
