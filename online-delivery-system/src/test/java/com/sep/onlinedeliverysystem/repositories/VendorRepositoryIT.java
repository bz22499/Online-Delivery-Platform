package com.sep.onlinedeliverysystem.repositories;

import com.sep.onlinedeliverysystem.TestUtil;
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
@DirtiesContext(classMode =  DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class VendorRepositoryIT {

    private VendorRepository vendorTest;

    @Autowired
    public VendorRepositoryIT(VendorRepository vendorTest) { this.vendorTest = vendorTest; }

    @Test
    public void testSingleVendorCreationAndFind(){
        Vendor vendor = TestUtil.vendorBuild1();
        vendorTest.save(vendor);
        Optional<Vendor> result = vendorTest.findById(vendor.getEmail());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(vendor);
    }

    @Test
    public void testMultipleVendorCreationAndFind(){
        Vendor vendor1 = TestUtil.vendorBuild1();
        Vendor vendor2 = TestUtil.vendorBuild2();
        Vendor vendor3 = TestUtil.vendorBuild3();
        vendorTest.save(vendor1);
        vendorTest.save(vendor2);
        vendorTest.save(vendor3);
        Iterable<Vendor> result = vendorTest.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(vendor1, vendor2, vendor3);
    }

    @Test
    public void testVendorUpdate(){
        Vendor vendor = TestUtil.vendorBuild1();
        vendorTest.save(vendor);
        vendor.setName("Great Food");
        vendorTest.save(vendor);
        Optional<Vendor> result = vendorTest.findById(vendor.getEmail());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(vendor);
    }

    @Test
    public void testVendorDelete(){
        Vendor vendor = TestUtil.vendorBuild1();
        vendorTest.save(vendor);
        vendorTest.delete(vendor);
        Optional<Vendor> result = vendorTest.findById(vendor.getEmail());
        assertThat(result).isEmpty();
    }
}
