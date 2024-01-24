package com.sep.onlinedeliverysystem.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.domain.dto.MenuItemDTO;
import com.sep.onlinedeliverysystem.domain.entities.MenuItem;
import com.sep.onlinedeliverysystem.domain.entities.Vendor;
import com.sep.onlinedeliverysystem.repositories.VendorRepository;
import com.sep.onlinedeliverysystem.services.MenuItemService;
import org.checkerframework.checker.units.qual.A;
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
public class MenuItemControllerIntegrationTests {

    @Autowired
    private VendorRepository vendorRepository;
    private MenuItemService menuItemService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public MenuItemControllerIntegrationTests(MockMvc mockMvc, MenuItemService menuItemService) {
        this.mockMvc = mockMvc;
        this.menuItemService = menuItemService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateItemSuccessfullyReturnsHttp201Created() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItem testItem1 = TestUtil.menuItemBuilder1(testVendor);
        String menuItemJson = objectMapper.writeValueAsString(testItem1);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/menuItems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(menuItemJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateItemEntitySuccessfullyReturnsSavedItem() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItem testItem1 = TestUtil.menuItemBuilder1(testVendor);
        String menuItemJson = objectMapper.writeValueAsString(testItem1);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/menuItems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(menuItemJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Toast")
        );
    }

    @Test
    public void testThatCreateItemDTOSuccessfullyReturnssavedItem() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItemDTO testItem1 = TestUtil.menuItemDTOCreate1(testVendor);
        String menuItemJson = objectMapper.writeValueAsString(testItem1);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/menuItems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(menuItemJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Toast")
        );
    }

    @Test
    public void testThatListItemSuccessfullyReturnsHttp200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/menuItems")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListItemSuccessfullyReturnsListOfMenuItems() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItem testItem1 = TestUtil.menuItemBuilder1(testVendor);
        menuItemService.save(testItem1);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/menuItems")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Toast")
        );
    }

    @Test
    public void testThatListItemsByVendorSuccessfullyReturnsListOfMenuItems() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItem testItem1 = TestUtil.menuItemBuilder1(testVendor);
        menuItemService.save(testItem1);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/menuItems/vendor/restaurant@foodmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").value("Toast")
        );
    }

    @Test
    public void testThatGetItemSuccessfullyReturnsHttpStatus200OkWhenItemExists() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItem testItem1 = TestUtil.menuItemBuilder1(testVendor);
        menuItemService.save(testItem1);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/menuItems/" + testItem1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetItemSuccessfullyReturnsHttpStatus404NotFoundWhenItemDoesNotExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/menuItems/123")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatGetItemSuccessfullyReturnsAddressWhenItemExists() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItem testItem1 = TestUtil.menuItemBuilder1(testVendor);
        menuItemService.save(testItem1);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/menuItems/" + testItem1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Toast")
        );
    }

    @Test
    public void testThatFullUpdateItemSuccessfullyReturnsHttpStatus404NotFoundWhenItemDoesNotExist() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItemDTO testItem1 = TestUtil.menuItemDTOCreate1(testVendor);
        String menuItemDTOJson = objectMapper.writeValueAsString(testItem1);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/menuItems/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(menuItemDTOJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateItemSuccessfullyReturnsHttpStatus200OKWhenItemExists() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItem testmenuItem1 = TestUtil.menuItemBuilder1(testVendor);
        MenuItem savedItem = menuItemService.save(testmenuItem1);
        MenuItemDTO testmenuItemDTO1 = TestUtil.menuItemDTOCreate1(testVendor);
        String menuItemDTOJson = objectMapper.writeValueAsString(testmenuItemDTO1);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/menuItems/" + savedItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(menuItemDTOJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatFullUpdateUpdatesExistingItem() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItem testmenuItem1 = TestUtil.menuItemBuilder1(testVendor);
        MenuItem savedItem = menuItemService.save(testmenuItem1);

        MenuItem menuItemDTO = TestUtil.menuItemBuilder2(testVendor);
        menuItemDTO.setId(savedItem.getId());
        String menuItemUpdateDTOJson = objectMapper.writeValueAsString(menuItemDTO);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/menuItems/" + savedItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(menuItemUpdateDTOJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedItem.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(menuItemDTO.getName())
        );
    }

    @Test
    public void testThatPartialUpdateItemSuccessfullyReturnsHttpStatus200OKWhenKItemExists() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItem testmenuItem = TestUtil.menuItemBuilder1(testVendor);
        MenuItem savedItem = menuItemService.save(testmenuItem);

        MenuItemDTO testmenuItemDTO = TestUtil.menuItemDTOCreate1(testVendor);
        testmenuItemDTO.setId(savedItem.getId());
        testmenuItemDTO.setName("UPDATED!!!");
        String menuItemDTOJson = objectMapper.writeValueAsString(testmenuItemDTO);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/menuItems/" + savedItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(menuItemDTOJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatPartialUpdateItemSuccessfullyReturnsUpdatedItem() throws Exception {
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItem testmenuItem = TestUtil.menuItemBuilder1(testVendor);
        MenuItem savedItem = menuItemService.save(testmenuItem);

        MenuItemDTO testmenuItemDTO = TestUtil.menuItemDTOCreate1(testVendor);
        testmenuItemDTO.setId(savedItem.getId());
        testmenuItemDTO.setName("UPDATED!!!");
        String menuItemDTOJson = objectMapper.writeValueAsString(testmenuItemDTO);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/menuItems/" + savedItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(menuItemDTOJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedItem.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.description").value(testmenuItemDTO.getDescription())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("UPDATED!!!")
        );
    }

    @Test
    public void testThatDeleteItemReturnsHttpStatus204ForNonExistingItem() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/menuItems/1235832")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteItemReturnsHttpStatus204ForExistingItem() throws Exception{
        Vendor testVendor = TestUtil.vendorBuild1();
        vendorRepository.save(testVendor);
        MenuItem testmenuItem = TestUtil.menuItemBuilder1(testVendor);
        MenuItem savedItem = menuItemService.save(testmenuItem);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/menuItems/" + savedItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }


}
