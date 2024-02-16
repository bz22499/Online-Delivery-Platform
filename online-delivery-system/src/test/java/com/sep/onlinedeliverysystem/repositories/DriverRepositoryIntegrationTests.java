package com.sep.onlinedeliverysystem.repositories;

import com.sep.onlinedeliverysystem.TestUtil;
import com.sep.onlinedeliverysystem.domain.entities.Driver;
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
@DirtiesContext(classMode =  DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DriverRepositoryIntegrationTests {

    private DriverRepository driverTest;

    @Autowired
    public DriverRepositoryIntegrationTests(DriverRepository driverTest) { this.driverTest = driverTest; }

    @Test
    public void testSingleDriverCreationAndFind(){
        Driver driver = TestUtil.driverBuild1();
        driverTest.save(driver);
        Optional<Driver> result = driverTest.findById(driver.getEmail());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(driver);
    }

    @Test
    public void testMultipleDriverCreationAndFind(){
        Driver driver1 = TestUtil.driverBuild1();
        Driver driver2 = TestUtil.driverBuild2();
        driverTest.save(driver1);
        driverTest.save(driver2);
        Iterable<Driver> result = driverTest.findAll();
        assertThat(result)
                .hasSize(2)
                .containsExactly(driver1, driver2);
    }

    @Test
    public void testVendorUpdate(){
        Driver driver = TestUtil.driverBuild1();
        driverTest.save(driver);
        driver.setName("Great Driver");
        driverTest.save(driver);
        Optional<Driver> result = driverTest.findById(driver.getEmail());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(driver);
    }

    @Test
    public void testVendorDelete(){
        Driver driver = TestUtil.driverBuild1();
        driverTest.save(driver);
        driverTest.delete(driver);
        Optional<Driver> result = driverTest.findById(driver.getEmail());
        assertThat(result).isEmpty();
    }
}
