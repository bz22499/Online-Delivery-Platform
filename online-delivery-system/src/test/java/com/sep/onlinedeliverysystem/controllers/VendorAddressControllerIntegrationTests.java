package com.sep.onlinedeliverysystem.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.domain.dto.VendorAddressDTO;
import com.sep.onlinedeliverysystem.domain.entities.Vendor;
import com.sep.onlinedeliverysystem.domain.entities.VendorAddress;
import com.sep.onlinedeliverysystem.services.VendorAddressService;
import com.sep.onlinedeliverysystem.services.VendorService;
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
public class VendorAddressControllerIntegrationTests {

    private VendorAddressService vendorAddressService;

    private VendorService vendorService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public VendorAddressControllerIntegrationTests(MockMvc mockMvc, VendorAddressService vendorAddressService, VendorService vendorService) {
        this.mockMvc = mockMvc;
        this.vendorAddressService = vendorAddressService;
        this.objectMapper = new ObjectMapper();
        this.vendorService = vendorService;
    }

    @Test
    public void testThatCreateAddressSuccessfullyReturnsHttp201Created() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorService.save(testVendor);
        VendorAddress testAddress1 = TestUtil.vendorAddressBuild1(testVendor);
        String addressJson = objectMapper.writeValueAsString(testAddress1);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/vendorAddresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateAddressEntitySuccessfullyReturnsSavedAddress() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorService.save(testVendor);
        VendorAddress testAddress1 = TestUtil.vendorAddressBuild1(testVendor);
        String addressJson = objectMapper.writeValueAsString(testAddress1);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/vendorAddresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.street").value("Food Street")
        );
    }

    @Test
    public void testThatCreateAddressDTOSuccessfullyReturnsSavedAddress() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorService.save(testVendor);
        VendorAddressDTO testAddress1 = TestUtil.vendorAddressDTOCreate1(testVendor);
        String addressJson = objectMapper.writeValueAsString(testAddress1);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/vendorAddresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.street").value("Food Street")
        );
    }

    @Test
    public void testThatListAddressSuccessfullyReturnsHttp200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/vendorAddresses")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListAddressSuccessfullyReturnsListOfAddresses() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorService.save(testVendor);
        VendorAddress testAddress1 = TestUtil.vendorAddressBuild1(testVendor);
        vendorAddressService.save(testAddress1);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/vendorAddresses")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].street").value("Food Street")
        );
    }

    @Test
    public void testThatGetAddressSuccessfullyReturnsHttpStatus200OkWhenAddressExists() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorService.save(testVendor);
        VendorAddress testAddress1 = TestUtil.vendorAddressBuild1(testVendor);
        vendorAddressService.save(testAddress1);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/vendorAddresses/" + testAddress1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetAddressSuccessfullyReturnsHttpStatus404NotFoundWhenAddressDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/vendorAddresses/123")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatGetAddressSuccessfullyReturnsAddressWhenAddressExists() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorService.save(testVendor);
        VendorAddress testAddress1 = TestUtil.vendorAddressBuild1(testVendor);
        vendorAddressService.save(testAddress1);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/vendorAddresses/" + testAddress1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.street").value("Food Street")
        );
    }

    @Test
    public void testThatFullUpdateAddressSuccessfullyReturnsHttpStatus404NotFoundWhenAddressDoesNotExist() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        VendorAddressDTO testAddress1 = TestUtil.vendorAddressDTOCreate1(testVendor);
        String addressDTOJson = objectMapper.writeValueAsString(testAddress1);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/vendorAddresses/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressDTOJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateAddressSuccessfullyReturnsHttpStatus200OKWhenAddressExists() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorService.save(testVendor);
        VendorAddress testvendorAddress1 = TestUtil.vendorAddressBuild1(testVendor);
        VendorAddress savedAddress = vendorAddressService.save(testvendorAddress1);
        VendorAddressDTO testvendorAddressDTO1 = TestUtil.vendorAddressDTOCreate1(testVendor);
        String addressDTOJson = objectMapper.writeValueAsString(testvendorAddressDTO1);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/vendorAddresses/" + savedAddress.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressDTOJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatFullUpdateUpdatesExistingAddress() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorService.save(testVendor);
        VendorAddress testvendorAddress1 = TestUtil.vendorAddressBuild1(testVendor);
        VendorAddress savedAddress = vendorAddressService.save(testvendorAddress1);

        VendorAddress addressDTO = TestUtil.vendorAddressBuild2(testVendor);
        addressDTO.setId(savedAddress.getId());
        String addressUpdateDTOJson = objectMapper.writeValueAsString(addressDTO);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/vendorAddresses/" + savedAddress.getId())
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

    @Test
    public void testThatPartialUpdateAddressSuccessfullyReturnsHttpStatus200OKWhenAddressExists() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorService.save(testVendor);
        VendorAddress testvendorAddress = TestUtil.vendorAddressBuild1(testVendor);
        VendorAddress savedAddress = vendorAddressService.save(testvendorAddress);

        VendorAddressDTO testvendorAddressDTO = TestUtil.vendorAddressDTOCreate1(testVendor);
        testvendorAddressDTO.setId(savedAddress.getId());
        testvendorAddressDTO.setStreet("UPDATED!!!");
        String addressDTOJson = objectMapper.writeValueAsString(testvendorAddressDTO);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/vendorAddresses/" + savedAddress.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressDTOJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatPartialUpdateAddressSuccessfullyReturnsUpdatedAddress() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorService.save(testVendor);
        VendorAddress testvendorAddress = TestUtil.vendorAddressBuild1(testVendor);
        VendorAddress savedAddress = vendorAddressService.save(testvendorAddress);

        VendorAddressDTO testvendorAddressDTO = TestUtil.vendorAddressDTOCreate1(testVendor);
        testvendorAddressDTO.setId(savedAddress.getId());
        testvendorAddressDTO.setStreet("UPDATED!!!");
        String addressDTOJson = objectMapper.writeValueAsString(testvendorAddressDTO);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/vendorAddresses/" + savedAddress.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressDTOJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAddress.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.postCode").value(testvendorAddressDTO.getPostCode())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.street").value("UPDATED!!!")
        );
    }

    @Test
    public void testThatDeleteAddressReturnsHttpStatus204ForNonExistingAddress() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/vendorAddresses/1235832")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteAddressReturnsHttpStatus204ForExistingAddress() throws Exception{
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorService.save(testVendor);
        VendorAddress testvendorAddress = TestUtil.vendorAddressBuild1(testVendor);
        VendorAddress savedAddress = vendorAddressService.save(testvendorAddress);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/vendorAddresses/" + savedAddress.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testGetAddressByVendorEmailReturnsHttpStatus200OkWhenAddressExists() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorService.save(testVendor);
        VendorAddress testAddress = TestUtil.vendorAddressBuild1(testVendor);
        vendorAddressService.save(testAddress);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/vendorAddresses/vendor/{email}", testVendor.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetAddressByVendorEmailReturnsHttpStatus404NotFoundWhenAddressDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/vendorAddresses/vendor/nonexistent@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testGetAddressByVendorEmailReturnsCorrectAddress() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorService.save(testVendor);

        VendorAddress testAddress = TestUtil.vendorAddressBuild1(testVendor);
        vendorAddressService.save(testAddress);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/vendorAddresses/vendor/{email}", testVendor.getEmail())
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.street").value(testAddress.getStreet()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postCode").value(testAddress.getPostCode()));
    }

    @Test
    public void testPartialUpdateAddressByVendorEmailReturnsHttpStatus200OkWhenAddressExists() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorService.save(testVendor);
        VendorAddress testAddress = TestUtil.vendorAddressBuild1(testVendor);
        vendorAddressService.save(testAddress);

        VendorAddress updatedAddress = TestUtil.vendorAddressBuild2(testVendor);
        updatedAddress.setId(testAddress.getId());
        updatedAddress.setStreet("Updated Street");

        String updatedAddressJson = objectMapper.writeValueAsString(updatedAddress);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/vendorAddresses/vendor/{email}", testVendor.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedAddressJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPartialUpdateAddressByVendorEmailReturnsHttpStatus404NotFoundWhenAddressDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/vendorAddresses/vendor/nonexistent@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}") // Empty body for non-existent address
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testPartialUpdateAddressByVendorEmailUpdatesCorrectly() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorService.save(testVendor);

        VendorAddress originalAddress = TestUtil.vendorAddressBuild1(testVendor);
        vendorAddressService.save(originalAddress);

        VendorAddress updatedAddress = TestUtil.vendorAddressBuild2(testVendor);
        updatedAddress.setId(originalAddress.getId());
        updatedAddress.setStreet("UPDATED!!!");

        String updatedAddressJson = objectMapper.writeValueAsString(updatedAddress);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/vendorAddresses/vendor/{email}", testVendor.getEmail())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatedAddressJson)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("UPDATED!!!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postCode").value(updatedAddress.getPostCode()));
    }

}
