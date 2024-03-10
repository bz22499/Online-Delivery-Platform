package com.sep.onlinedeliverysystem.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.domain.dto.UserAddressDTO;
import com.sep.onlinedeliverysystem.domain.entities.User;
import com.sep.onlinedeliverysystem.domain.entities.UserAddress;
import com.sep.onlinedeliverysystem.services.UserAddressService;
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

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserAddressControllerIntegrationTests {

    private UserAddressService userAddressService;
    private UserService userService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public UserAddressControllerIntegrationTests(MockMvc mockMvc, UserAddressService userAddressService, UserService userService) {
        this.mockMvc = mockMvc;
        this.userAddressService = userAddressService;
        this.userService = userService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateAddressSuccessfullyReturnsHttp201Created() throws Exception {
        User testUser = TestUtil.userBuild1();
        userService.save(testUser);
        UserAddress testAddress1 = TestUtil.userAddressBuild1(testUser);
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
        User testUser = TestUtil.userBuild1();
        userService.save(testUser);
        UserAddress testAddress1 = TestUtil.userAddressBuild1(testUser);
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
        User testUser = TestUtil.userBuild1();
        userService.save(testUser);
        UserAddressDTO testAddress1 = TestUtil.userAddressDTOCreate1(testUser);
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
        User testUser = TestUtil.userBuild1();
        userService.save(testUser);
        UserAddress testAddress1 = TestUtil.userAddressBuild1(testUser);
        userAddressService.save(testAddress1);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/addresses")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].street").value("123 Kiggell Road")
        );
    }

    @Test
    public void testThatGetAddressSuccessfullyReturnsHttpStatus200OkWhenAddressExists() throws Exception {
        User testUser = TestUtil.userBuild1();
        userService.save(testUser);
        UserAddress testAddress1 = TestUtil.userAddressBuild1(testUser);
        userAddressService.save(testAddress1);
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
        User testUser = TestUtil.userBuild1();
        userService.save(testUser);
        UserAddress testAddress1 = TestUtil.userAddressBuild1(testUser);
        userAddressService.save(testAddress1);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/addresses/" + testAddress1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.street").value("123 Kiggell Road")
        );
    }

    @Test
    public void testThatFullUpdateAddressSuccessfullyReturnsHttpStatus404NotFoundWhenAddressDoesNotExist() throws Exception {
        User testUser = TestUtil.userBuild1();
        userService.save(testUser);
        UserAddressDTO testAddress1 = TestUtil.userAddressDTOCreate1(testUser);
        String addressDTOJson = objectMapper.writeValueAsString(testAddress1);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/addresses/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressDTOJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateAddressSuccessfullyReturnsHttpStatus200OKWhenAddressExists() throws Exception {
        User testUser = TestUtil.userBuild1();
        userService.save(testUser);
        User testUser2 = TestUtil.userBuild2();
        userService.save(testUser2);
        UserAddress testUserAddress1 = TestUtil.userAddressBuild1(testUser);
        UserAddress savedAddress = userAddressService.save(testUserAddress1);
        UserAddressDTO testUserAddressDTO1 = TestUtil.userAddressDTOCreate1(testUser2);
        String addressDTOJson = objectMapper.writeValueAsString(testUserAddressDTO1);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/addresses/" + savedAddress.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressDTOJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatFullUpdateUpdatesExistingAddress() throws Exception {
        User testUser = TestUtil.userBuild1();
        userService.save(testUser);
        User testUser2 = TestUtil.userBuild2();
        userService.save(testUser2);
        UserAddress testUserAddress1 = TestUtil.userAddressBuild1(testUser);
        UserAddress savedAddress = userAddressService.save(testUserAddress1);

        UserAddress addressDTO = TestUtil.userAddressBuild2(testUser2);
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

    @Test
    public void testThatPartialUpdateAddressSuccessfullyReturnsHttpStatus200OKWhenAddressExists() throws Exception {
        User testUser = TestUtil.userBuild1();
        userService.save(testUser);
        User testUser2 = TestUtil.userBuild2();
        userService.save(testUser2);
        UserAddress testUserAddress = TestUtil.userAddressBuild1(testUser);
        UserAddress savedAddress = userAddressService.save(testUserAddress);

        UserAddressDTO testUserAddressDTO = TestUtil.userAddressDTOCreate1(testUser2);
        testUserAddressDTO.setId(savedAddress.getId());
        testUserAddressDTO.setStreet("UPDATED!!!");
        String addressDTOJson = objectMapper.writeValueAsString(testUserAddressDTO);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/addresses/" + savedAddress.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressDTOJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatPartialUpdateAddressSuccessfullyReturnsUpdatedAddress() throws Exception {
        User testUser = TestUtil.userBuild1();
        userService.save(testUser);
        User testUser2 = TestUtil.userBuild2();
        userService.save(testUser2);
        UserAddress testUserAddress = TestUtil.userAddressBuild1(testUser);
        UserAddress savedAddress = userAddressService.save(testUserAddress);

        UserAddressDTO testUserAddressDTO = TestUtil.userAddressDTOCreate1(testUser2);
        testUserAddressDTO.setId(savedAddress.getId());
        testUserAddressDTO.setStreet("UPDATED!!!");
        String addressDTOJson = objectMapper.writeValueAsString(testUserAddressDTO);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/addresses/" + savedAddress.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressDTOJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedAddress.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.postCode").value(testUserAddressDTO.getPostCode())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.street").value("UPDATED!!!")
        );
    }

    @Test
    public void testThatDeleteAddressReturnsHttpStatus204ForNonExistingAddress() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/addresses/1235832")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteAddressReturnsHttpStatus204ForExistingAddress() throws Exception{
        User testUser = TestUtil.userBuild1();
        userService.save(testUser);
        UserAddress testUserAddress = TestUtil.userAddressBuild1(testUser);
        UserAddress savedAddress = userAddressService.save(testUserAddress);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/addresses/" + savedAddress.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testGetAddressByUserEmailReturnsHttpStatus200OkWhenAddressExists() throws Exception {
        User testUser = TestUtil.userBuild1();
        userService.save(testUser);
        UserAddress testAddress = TestUtil.userAddressBuild1(testUser);
        userAddressService.save(testAddress);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/addresses/user/{email}", testUser.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetAddressByUserEmailReturnsHttpStatus404NotFoundWhenAddressDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/addresses/user/nonexistent@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testGetAddressByUserEmailReturnsCorrectAddress() throws Exception {
        User testUser = TestUtil.userBuild1();
        userService.save(testUser);

        UserAddress testAddress = TestUtil.userAddressBuild1(testUser);
        userAddressService.save(testAddress);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/addresses/user/{email}", testUser.getEmail())
                                .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.street").value(testAddress.getStreet()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postCode").value(testAddress.getPostCode()));
    }

    @Test
    public void testPartialUpdateAddressByUserEmailReturnsHttpStatus200OkWhenAddressExists() throws Exception {
        User testUser = TestUtil.userBuild1();
        userService.save(testUser);
        UserAddress testAddress = TestUtil.userAddressBuild1(testUser);
        userAddressService.save(testAddress);

        UserAddress updatedAddress = TestUtil.userAddressBuild2(testUser);
        updatedAddress.setId(testAddress.getId());
        updatedAddress.setStreet("Updated Street");

        String updatedAddressJson = objectMapper.writeValueAsString(updatedAddress);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/addresses/user/{email}", testUser.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedAddressJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testPartialUpdateAddressByUserEmailReturnsHttpStatus404NotFoundWhenAddressDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/addresses/user/nonexistent@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}") // Empty body for non-existent address
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testPartialUpdateAddressByUserEmailUpdatesCorrectly() throws Exception {
        User testUser = TestUtil.userBuild1();
        userService.save(testUser);

        UserAddress originalAddress = TestUtil.userAddressBuild1(testUser);
        userAddressService.save(originalAddress);

        UserAddress updatedAddress = TestUtil.userAddressBuild2(testUser);
        updatedAddress.setId(originalAddress.getId());
        updatedAddress.setStreet("UPDATED!!!");

        String updatedAddressJson = objectMapper.writeValueAsString(updatedAddress);
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/addresses/user/{email}", testUser.getEmail())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(updatedAddressJson)
                ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.street").value("UPDATED!!!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postCode").value(updatedAddress.getPostCode()));
    }

}
