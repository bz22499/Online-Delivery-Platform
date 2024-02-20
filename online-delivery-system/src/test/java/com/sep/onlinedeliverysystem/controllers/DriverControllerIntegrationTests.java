package com.sep.onlinedeliverysystem.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.domain.dto.DriverDTO;
import com.sep.onlinedeliverysystem.domain.entities.Driver;
import com.sep.onlinedeliverysystem.services.DriverService;
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
public class DriverControllerIntegrationTests {

    private DriverService driverService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public DriverControllerIntegrationTests(MockMvc mockMvc, DriverService driverService) {
        this.driverService = driverService;
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateDriverSuccessfullyReturnsHttp201Created() throws Exception {
        Driver testDriver1 = TestUtil.driverBuild1();
        String driverJson = objectMapper.writeValueAsString(testDriver1);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(driverJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateDriverSuccessfullyReturnsSavedDriver() throws Exception {
        Driver testDriver1 = TestUtil.driverBuild1();
        String driverJson = objectMapper.writeValueAsString(testDriver1);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(driverJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value("jiahao@bristol.com")
        );
    }

    @Test
    public void testThatListDriverSuccessfullyReturnsHttpStatus200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatListDriverSuccessfullyReturnsListOfDrivers() throws Exception {
        Driver testDriver1 = TestUtil.driverBuild1();
        driverService.save(testDriver1);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/drivers")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].email").value("jiahao@bristol.com")
        );
    }

    @Test
    public void testThatGetDriverSuccessfullyReturnsHttpStatus200OkWhenDriverExists() throws Exception {
        Driver testDriver1 = TestUtil.driverBuild1();
        driverService.save(testDriver1);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/drivers/jiahao@bristol.com")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetDriverSuccessfullyReturnsHttpStatus404NotFoundWhenDriverDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/drivers/incorrectemail")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatGetDriverSuccessfullyReturnsDriverWhenDriverExists() throws Exception {
        Driver testDriver1 = TestUtil.driverBuild1();
        driverService.save(testDriver1);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/drivers/jiahao@bristol.com")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value("jiahao@bristol.com")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Jia")
        );
    }

    @Test
    public void testThatFullUpdateDriverSuccessfullyReturnsHttpStatus404NotFoundWhenDriverDoesNotExist() throws Exception {
        DriverDTO testDriver1 = TestUtil.driverDTOCreate1();
        String driverDTOJson = objectMapper.writeValueAsString(testDriver1);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/drivers/incorrect_email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(driverDTOJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateDriverSuccessfullyReturnsHttpStatus200OKWhenDriverExists() throws Exception {
        Driver testDriverEntity1 = TestUtil.driverBuild1();
        Driver savedDriver = driverService.save(testDriverEntity1);
        DriverDTO testDriverDTO1 = TestUtil.driverDTOCreate1();
        String driverDTOJson = objectMapper.writeValueAsString(testDriverDTO1);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/drivers/" + savedDriver.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(driverDTOJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatFullUpdateUpdatesExistingDriver() throws Exception {
        Driver testDriverEntity1 = TestUtil.driverBuild1();
        Driver savedDriver = driverService.save(testDriverEntity1);

        Driver driverDTO = TestUtil.driverBuild2();
        driverDTO.setEmail(savedDriver.getEmail());
        String driverUpdateDTOJson = objectMapper.writeValueAsString(driverDTO);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/drivers/" + savedDriver.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(driverUpdateDTOJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(savedDriver.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(driverDTO.getName())
        );
    }

    @Test
    public void testThatPartialUpdateDriverSuccessfullyReturnsHttpStatus200OKWhenDriverExists() throws Exception {
        Driver testDriverEntity1 = TestUtil.driverBuild1();
        Driver savedDriver = driverService.save(testDriverEntity1);

        DriverDTO testDriverDTO1 = TestUtil.driverDTOCreate1();
        testDriverDTO1.setName("UPDATED!!!");
        String driverDTOJson = objectMapper.writeValueAsString(testDriverDTO1);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/drivers/" + savedDriver.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(driverDTOJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatPartialUpdateDriverSuccessfullyReturnsUpdatedDriver() throws Exception {
        Driver testDriverEntity1 = TestUtil.driverBuild1();
        Driver savedDriver = driverService.save(testDriverEntity1);

        DriverDTO testDriverDTO1 = TestUtil.driverDTOCreate1();
        testDriverDTO1.setName("UPDATED!!!");
        String driverDTOJson = objectMapper.writeValueAsString(testDriverDTO1);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/drivers/" + savedDriver.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(driverDTOJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(savedDriver.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("UPDATED!!!")
        );
    }

    @Test
    public void testThatDeleteDriverReturnsHttpStatus204ForNonExistingDriver() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/drivers/incorrectemail")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteDriverReturnsHttpStatus204ForExistingDriver() throws Exception {
        Driver testDriverEntity1 = TestUtil.driverBuild1();
        Driver savedDriver = driverService.save(testDriverEntity1);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/drivers/" + savedDriver.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }



}

