package com.sep.onlinedeliverysystem.repositories;

import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.domain.entities.Vendor;
import com.sep.onlinedeliverysystem.domain.entities.VendorAddress;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class VendorAddressRepositoryIntegrationTests {

    private VendorRepository vendor;
    private VendorAddressRepository vendorAddressTest;

    @Autowired
    public VendorAddressRepositoryIntegrationTests(VendorAddressRepository vendorAddressTest, VendorRepository vendor){
        this.vendorAddressTest = vendorAddressTest;
        this.vendor = vendor;
    }

    @Test
    public void testSingleVendorAddressCreationAndFind(){
        Vendor vendor = TestUtil.vendorBuild1();
        VendorAddress vendorAddress = TestUtil.vendorAddressBuild1(vendor);
        vendorAddressTest.save(vendorAddress);
        Optional<VendorAddress> result = vendorAddressTest.findById(vendorAddress.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(vendorAddress);
    }

    @Test
    public void testMultipleVendorAddressCreationAndFind(){
        Vendor vendor = TestUtil.vendorBuild1();
        VendorAddress vendorAddress1 = TestUtil.vendorAddressBuild1(vendor);
        VendorAddress vendorAddress2 = TestUtil.vendorAddressBuild2(vendor);
        VendorAddress vendorAddress3 = TestUtil.vendorAddressBuild3(vendor);
        vendorAddressTest.save(vendorAddress1);
        vendorAddressTest.save(vendorAddress2);
        vendorAddressTest.save(vendorAddress3);
        Iterable<VendorAddress> result = vendorAddressTest.findAll();
        assertThat(result)
                .hasSize(3)
                .containsExactly(vendorAddress1, vendorAddress2, vendorAddress3);
    }

    @Test
    public void testVendorAddressUpdate(){
        Vendor vendor = TestUtil.vendorBuild1();
        VendorAddress vendorAddress1 = TestUtil.vendorAddressBuild1(vendor);
        vendorAddressTest.save(vendorAddress1);
        vendorAddress1.setCountry("Wales");
        vendorAddressTest.save(vendorAddress1);
        Optional<VendorAddress> result = vendorAddressTest.findById(vendorAddress1.getId());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(vendorAddress1);
    }

    @Test
    public void testVendorAddressDelete(){
        Vendor vendor = TestUtil.vendorBuild1();
        VendorAddress vendorAddress1 = TestUtil.vendorAddressBuild1(vendor);
        vendorAddressTest.save(vendorAddress1);
        vendorAddressTest.delete(vendorAddress1);
        Optional<VendorAddress> result = vendorAddressTest.findById(vendorAddress1.getId());
        assertThat(result).isEmpty();
    }
}