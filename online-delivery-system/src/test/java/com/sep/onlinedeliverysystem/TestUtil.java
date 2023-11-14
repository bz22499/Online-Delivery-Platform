package com.sep.onlinedeliverysystem;

import com.sep.onlinedeliverysystem.model.Address;
import com.sep.onlinedeliverysystem.model.User;

public final class TestUtil {
    private TestUtil(){

    }

    public static User userBuild() {
        User user = User.builder()
            .id(1L)
            .first_name("Luke")
            .last_name("Trott")
            .email("luke@trottmail.com")
            .password("password")
            .role("customer")
            .build();
        return user;
    }

    public static Address addressBuild1() {
        Address address = Address.builder()
                .id(2L)
                .userId(1L)
                .street("123 Kiggell Road")
                .city("Bristol")
                .postCode("A12 B34")
                .country("Wales")
                .build();
        return address;
    }

    public static Address addressBuild2() {
        Address address = Address.builder()
                .id(3L)
                .userId(1L)
                .street("123 Pierce Road")
                .city("Bristol")
                .postCode("C12 D34")
                .country("Wales")
                .build();
        return address;
    }

    public static Address addressBuild3() {
        Address address = Address.builder()
                .id(4L)
                .userId(1L)
                .street("123 Li Road")
                .city("Bristol")
                .postCode("E12 F34")
                .country("Wales")
                .build();
        return address;
    }
}
