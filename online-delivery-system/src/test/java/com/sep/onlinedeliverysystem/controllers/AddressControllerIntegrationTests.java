package com.sep.onlinedeliverysystem.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.domain.dto.AddressDTO;
import com.sep.onlinedeliverysystem.domain.dto.UserDTO;
import com.sep.onlinedeliverysystem.domain.entities.AddressEntity;
import com.sep.onlinedeliverysystem.domain.entities.UserEntity;
import com.sep.onlinedeliverysystem.services.AddressService;
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

    private AddressService addressService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public AddressControllerIntegrationTests(MockMvc mockMvc, AddressService addressService) {
        this.mockMvc = mockMvc;
        this.addressService = addressService;
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
    public void testThatCreateAddressEntitySuccessfullyReturnsSavedAddress() throws Exception {
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
    public void testThatCreateAddressDTOSuccessfullyReturnsSavedAddress() throws Exception {
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

    @Test
    public void testThatListAddressSuccessfullyReturnsHttp200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListAddressSuccessfullyReturnsListOfAddresses() throws Exception {
        AddressEntity testAddress1 = TestUtil.addressBuild1(null);
        addressService.save(testAddress1);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].street").value("123 Kiggell Road")
        );
    }

    @Test
    public void testThatGetAddressSuccessfullyReturnsHttpStatus200OkWhenAddressExists() throws Exception {
        AddressEntity testAddress1 = TestUtil.addressBuild1(null);
        addressService.save(testAddress1);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/addresses/" + testAddress1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetAddressSuccessfullyReturnsHttpStatus404NotFoundWhenAddressDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/addresses/123")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatGetAddressSuccessfullyReturnsAddressWhenAddressExists() throws Exception {
        AddressEntity testAddress1 = TestUtil.addressBuild1(null);
        addressService.save(testAddress1);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/addresses/" + testAddress1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.street").value("123 Kiggell Road")
        );
    }

    @Test
    public void testThatFullUpdateAddressSuccessfullyReturnsHttpStatus404NotFoundWhenAddressDoesNotExist() throws Exception {
        AddressDTO testAddress1 = TestUtil.addressDTOCreate1(null);
        String addressDTOJson = objectMapper.writeValueAsString(testAddress1);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/addresses/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressDTOJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateAddressSuccessfullyReturnsHttpStatus200OKWhenAddressExists() throws Exception {
        AddressEntity testAddressEntity1 = TestUtil.addressBuild1(null);
        AddressEntity savedAddress = addressService.save(testAddressEntity1);
        AddressDTO testAddressDTO1 = TestUtil.addressDTOCreate1(null);
        String addressDTOJson = objectMapper.writeValueAsString(testAddressDTO1);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/addresses/" + savedAddress.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressDTOJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatFullUpdateUpdatesExistingAddress() throws Exception {
        AddressEntity testAddressEntity1 = TestUtil.addressBuild1(null);
        AddressEntity savedAddress = addressService.save(testAddressEntity1);

        AddressEntity addressDTO = TestUtil.addressBuild2(null);
        addressDTO.setId(savedAddress.getId());
        String addressUpdateDTOJson = objectMapper.writeValueAsString(addressDTO);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/addresses/" + savedAddress.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressUpdateDTOJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAddress.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.street").value(addressDTO.getStreet())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.postCode").value(addressDTO.getPostCode())
        );
    }

}
