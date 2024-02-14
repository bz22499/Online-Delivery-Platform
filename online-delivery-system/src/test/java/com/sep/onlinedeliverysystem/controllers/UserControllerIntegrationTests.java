package com.sep.onlinedeliverysystem.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.domain.dto.UserDTO;
import com.sep.onlinedeliverysystem.domain.entities.User;
import com.sep.onlinedeliverysystem.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

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
        User testUser1 = TestUtil.userBuild1();
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
        User testUser1 = TestUtil.userBuild1();
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
        User testUser1 = TestUtil.userBuild1();
        userService.save(testUser1);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].email").value("luke@trottmail.com")
        );
    }

    @Test
    public void testThatGetUserSuccessfullyReturnsHttpStatus200OkWhenUserExists() throws Exception {
        User testUser1 = TestUtil.userBuild1();
        userService.save(testUser1);
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
        User testUser1 = TestUtil.userBuild1();
        userService.save(testUser1);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/luke@trottmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value("luke@trottmail.com")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstName").value("Luke")
        );
    }

    @Test
    public void testThatFullUpdateUserSuccessfullyReturnsHttpStatus404NotFoundWhenUserDoesNotExist() throws Exception {
        UserDTO testUser1 = TestUtil.userDTOCreate1();
        String userDTOJson = objectMapper.writeValueAsString(testUser1);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/incorrect_email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDTOJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateUserSuccessfullyReturnsHttpStatus200OKWhenUserExists() throws Exception {
        User testUserEntity1 = TestUtil.userBuild1();
        User savedUser = userService.save(testUserEntity1);
        UserDTO testUserDTO1 = TestUtil.userDTOCreate1();
        String userDTOJson = objectMapper.writeValueAsString(testUserDTO1);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/" + savedUser.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDTOJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatFullUpdateUpdatesExistingUser() throws Exception {
        User testUserEntity1 = TestUtil.userBuild1();
        User savedUser = userService.save(testUserEntity1);

        User userDTO = TestUtil.userBuild2();
        userDTO.setEmail(savedUser.getEmail());
        String userUpdateDTOJson = objectMapper.writeValueAsString(userDTO);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/" + savedUser.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userUpdateDTOJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(savedUser.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.lastName").value(userDTO.getLastName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstName").value(userDTO.getFirstName())
        );
    }

    @Test
    public void testThatPartialUpdateUserSuccessfullyReturnsHttpStatus200OKWhenUserExists() throws Exception {
        User testUserEntity1 = TestUtil.userBuild1();
        User savedUser = userService.save(testUserEntity1);

        UserDTO testUserDTO1 = TestUtil.userDTOCreate1();
        testUserDTO1.setFirstName("UPDATED!!!");
        String userDTOJson = objectMapper.writeValueAsString(testUserDTO1);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/users/" + savedUser.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDTOJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatPartialUpdateUserSuccessfullyReturnsUpdatedUser() throws Exception {
        User testUserEntity1 = TestUtil.userBuild1();
        User savedUser = userService.save(testUserEntity1);

        UserDTO testUserDTO1 = TestUtil.userDTOCreate1();
        testUserDTO1.setFirstName("UPDATED!!!");
        String userDTOJson = objectMapper.writeValueAsString(testUserDTO1);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/users/" + savedUser.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDTOJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(savedUser.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.lastName").value(testUserDTO1.getLastName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstName").value("UPDATED!!!")
        );
    }

    @Test
    public void testThatDeleteUserReturnsHttpStatus204ForNonExistingUser() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/incorrectemail")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteUserReturnsHttpStatus204ForExistingUser() throws Exception {
        User testUserEntity1 = TestUtil.userBuild1();
        User savedUser = userService.save(testUserEntity1);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/" + savedUser.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    @Test
    public void testThatUpdateProfileSuccessfullyReturnsHttpStatus200Ok() throws Exception {
        // Prepare test data
        User testUserEntity1 = TestUtil.userBuild1();
        userService.save(testUserEntity1);
        String email = testUserEntity1.getEmail();

        // Prepare request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("currentPassword", "password");
        requestBody.put("firstName", "UpdatedFirstName");
        requestBody.put("lastName", "UpdatedLastName");
        requestBody.put("newPassword", "newPassword");

        // Convert request body to JSON
        String requestBodyJson = objectMapper.writeValueAsString(requestBody);

        // Perform the request
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/users/" + email + "/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBodyJson)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("UpdatedFirstName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("UpdatedLastName"));
    }

    @Test
    public void testThatUpdateProfileReturnsUnauthorizedForIncorrectCurrentPassword() throws Exception {
        // Prepare test data
        User testUserEntity1 = TestUtil.userBuild1();
        userService.save(testUserEntity1);
        String email = testUserEntity1.getEmail();

        // Prepare request body with incorrect current password
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("currentPassword", "incorrectPassword");
        requestBody.put("firstName", "UpdatedFirstName");
        requestBody.put("lastName", "UpdatedLastName");
        requestBody.put("newPassword", "newPassword");

        // Convert request body to JSON
        String requestBodyJson = objectMapper.writeValueAsString(requestBody);

        // Perform the request
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/users/" + email + "/profile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBodyJson)
                ).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}

