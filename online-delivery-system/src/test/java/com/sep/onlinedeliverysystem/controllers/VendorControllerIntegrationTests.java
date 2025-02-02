package com.sep.onlinedeliverysystem.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.domain.dto.VendorDTO;
import com.sep.onlinedeliverysystem.domain.entities.Vendor;
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

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class VendorControllerIntegrationTests {

    private VendorService vendorService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public VendorControllerIntegrationTests(MockMvc mockMvc, VendorService vendorService) {
        this.vendorService = vendorService;
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateVendorSuccessfullyReturnsHttp201Created() throws Exception {
        Vendor testVendor1 = TestUtil.vendorBuild1();
        String vendorJson = objectMapper.writeValueAsString(testVendor1);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/vendors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(vendorJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateVendorSuccessfullyReturnsSavedUser() throws Exception {
        Vendor testVendor1 = TestUtil.vendorBuild1();
        String vendorJson = objectMapper.writeValueAsString(testVendor1);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/vendors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(vendorJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value("restaurant@foodmail.com")
        );
    }

    @Test
    public void testThatListVendorSuccessfullyReturnsHttpStatus200Ok() throws Exception {
           mockMvc.perform(
                   MockMvcRequestBuilders.get("/vendors")
                           .contentType(MediaType.APPLICATION_JSON)
           ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetVendorSuccessfullyReturnsHttpStatus200OkWhenUserExists() throws Exception {
        Vendor testVendor1 = TestUtil.vendorBuild1();
        vendorService.save(testVendor1);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/vendors/restaurant@foodmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetVendorSuccessfullyReturnsHttpStatus404NotFoundWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/vendors/incorrectemail")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatGetVendorSuccessfullyReturnsVendorWhenVendorExists() throws Exception {
        Vendor testVendor1 = TestUtil.vendorBuild1();
        vendorService.save(testVendor1);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/vendors/restaurant@foodmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value("restaurant@foodmail.com")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("WeSellFood")
        );
    }

    @Test
    public void listVendorsReturnsVendors() throws Exception {
        Vendor order1 = TestUtil.vendorBuild1();
        Vendor order2 = TestUtil.vendorBuild2();
        Vendor order3 = TestUtil.vendorBuild3();

        vendorService.save(order1);
        vendorService.save(order2);
        vendorService.save(order3);

        mockMvc.perform(MockMvcRequestBuilders.get("/vendors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(3))); // Ensure the response contains 3 orders
    }

    @Test
    public void testThatFullUpdateVendorSuccessfullyReturnsHttpStatus404NotFoundWhenUserDoesNotExist() throws Exception {
        Vendor testVendor1 = TestUtil.vendorBuild1();
        String vendorJson = objectMapper.writeValueAsString(testVendor1);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/vendors/incorrect_email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(vendorJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateVendorSuccessfullyReturnsHttpStatus200OKWhenUserExists() throws Exception {
        Vendor testVendor1 = TestUtil.vendorBuild1();
        Vendor savedVendor = vendorService.save(testVendor1);
        String vendorJson = objectMapper.writeValueAsString(testVendor1);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/vendors/" + savedVendor.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(vendorJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatFullUpdateUpdatesExistingVendor() throws Exception {
        Vendor testVendorEntity1 = TestUtil.vendorBuild1();
        Vendor savedVendor = vendorService.save(testVendorEntity1);

        Vendor vendor2 = TestUtil.vendorBuild2();
        vendor2.setEmail(savedVendor.getEmail());
        String userUpdateDTOJson = objectMapper.writeValueAsString(vendor2);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/vendors/" + savedVendor.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userUpdateDTOJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(savedVendor.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(vendor2.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.rating").value(vendor2.getRating())
        );
    }

    @Test
    public void testThatPartialUpdateVendorSuccessfullyReturnsHttpStatus200OKWhenUserExists() throws Exception {
        Vendor testVendorEntity1 = TestUtil.vendorBuild1();
        Vendor savedVendor = vendorService.save(testVendorEntity1);

        Vendor vendorToUpdate = TestUtil.vendorBuild2();
        vendorToUpdate.setName("UPDATED!!!");
        String vendorToUpdateJson = objectMapper.writeValueAsString(vendorToUpdate);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/vendors/" + savedVendor.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(vendorToUpdateJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatPartialUpdateVendorSuccessfullyReturnsUpdatedUser() throws Exception {
        Vendor testVendorEntity1 = TestUtil.vendorBuild1();
        Vendor savedVendor = vendorService.save(testVendorEntity1);

        VendorDTO testVendorDTO1 = TestUtil.vendorDTOBuild1();
        testVendorDTO1.setDescription("UPDATED!!!");
        String userDTOJson = objectMapper.writeValueAsString(testVendorDTO1);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/vendors/" + savedVendor.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDTOJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(savedVendor.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.rating").value(testVendorDTO1.getRating())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value("UPDATED!!!")
        );
    }

    @Test
    public void testThatDeleteVendorReturnsHttpStatus204ForNonExistingUser() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/vendors/incorrectemail")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteVendorReturnsHttpStatus204ForExistingUser() throws Exception{
        Vendor testVendorEntity1 = TestUtil.vendorBuild1();
        Vendor savedVendor = vendorService.save(testVendorEntity1);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/vendors/" + savedVendor.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatUpdateProfileSuccessfullyReturnsHttpStatus200Ok() throws Exception {
        Vendor testVendorEntity1 = TestUtil.vendorBuild1();
        vendorService.save(testVendorEntity1);
        String email = testVendorEntity1.getEmail();

        // Make request body
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("currentPassword", "password");
        requestBody.put("name", "UpdatedVendorName");
        requestBody.put("description", "UpdatedDescription");
        requestBody.put("newPassword", "newPassword");

        // Convert request body to JSON
        String requestBodyJson = objectMapper.writeValueAsString(requestBody);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/vendors/" + email + "/vendorProfile")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBodyJson)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(email))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("UpdatedVendorName"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("UpdatedDescription"));
    }

    @Test
    public void testThatUpdateProfileReturnsUnauthorizedForIncorrectCurrentPassword() throws Exception {
        Vendor testVendorEntity1 = TestUtil.vendorBuild1();
        vendorService.save(testVendorEntity1);
        String email = testVendorEntity1.getEmail();

        // Make request body with incorrect current password
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("currentPassword", "incorrectPassword");
        requestBody.put("name", "UpdatedVendorName");
        requestBody.put("description", "UpdatedDescription");
        requestBody.put("newPassword", "newPassword");

        // Convert request body to JSON
        String requestBodyJson = objectMapper.writeValueAsString(requestBody);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/vendors/" + email + "/vendorProfile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyJson)
        ).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
