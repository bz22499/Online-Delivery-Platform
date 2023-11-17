package com.sep.onlinedeliverysystem.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.domain.dto.AddressDTO;
import com.sep.onlinedeliverysystem.domain.entities.AddressEntity;
import com.sep.onlinedeliverysystem.domain.entities.UserEntity;
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

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class AddressControllerIntegrationTests {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public AddressControllerIntegrationTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateAddressSuccessfullyReturnsHttp201Created() throws Exception {
        AddressEntity testAddress1 = TestUtil.addressBuild1(null);
        String addressJson = objectMapper.writeValueAsString(testAddress1);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateAddressEntitySuccessfullyReturnsSavedUser() throws Exception {
        AddressEntity testAddress1 = TestUtil.addressBuild1(null);
        String addressJson = objectMapper.writeValueAsString(testAddress1);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.street").value("123 Kiggell Road")
        );
    }

    @Test
    public void testThatCreateAddressDTOSuccessfullyReturnsSavedUser() throws Exception {
        AddressDTO testAddress1 = TestUtil.addressDTOCreate1(null);
        String addressJson = objectMapper.writeValueAsString(testAddress1);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.street").value("123 Kiggell Road")
        );
    }
}
