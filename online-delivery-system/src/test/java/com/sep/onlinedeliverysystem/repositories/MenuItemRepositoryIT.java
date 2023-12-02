package com.sep.onlinedeliverysystem.repositories;

import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.domain.entities.MenuItem;
import com.sep.onlinedeliverysystem.domain.entities.Vendor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MenuItemRepositoryIT {

    @Autowired
    private VendorRepository vendorRepository;
    private MenuItemRepository menuItemTest;

    @Autowired
    public MenuItemRepositoryIT(MenuItemRepository menuItemTest){
        this.menuItemTest = menuItemTest;
    }

    @Test
    public void testSingleMenuItemCreationAndFind(){
        Vendor vendor = TestUtil.vendorBuild1();
        vendorRepository.save(vendor);
        MenuItem item = TestUtil.menuItemBuilder1(vendor);
        menuItemTest.save(item);
        Optional<MenuItem> result = menuItemTest.findById(item.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(item);
    }

    @Test
    public void testMultipleMenuItemCreationAndFind(){
        Vendor vendor = TestUtil.vendorBuild1();
        vendorRepository.save(vendor);
        MenuItem item1 = TestUtil.menuItemBuilder1(vendor);
        MenuItem item2 = TestUtil.menuItemBuilder2(vendor);
        MenuItem item3 = TestUtil.menuItemBuilder3(vendor);
        menuItemTest.save(item1);
        menuItemTest.save(item2);
        menuItemTest.save(item3);
        Iterable<MenuItem> result = menuItemTest.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(item1, item2, item3);
    }

    @Test
    public void testMenuItemUpdate(){
        Vendor vendor = TestUtil.vendorBuild1();
        vendorRepository.save(vendor);
        MenuItem item = TestUtil.menuItemBuilder1(vendor);
        menuItemTest.save(item);
        item.setPrice(19.99F);
        menuItemTest.save(item);
        Optional<MenuItem> result = menuItemTest.findById(item.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(item);
    }

    @Test
    public void testMenuItemDelete(){
        Vendor vendor = TestUtil.vendorBuild1();
        vendorRepository.save(vendor);
        MenuItem item = TestUtil.menuItemBuilder1(vendor);
        menuItemTest.save(item);
        menuItemTest.delete(item);
        Optional<MenuItem> result = menuItemTest.findById(item.getId());
        assertThat(result).isEmpty();
    }
}