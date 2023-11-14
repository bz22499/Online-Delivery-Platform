package com.sep.onlinedeliverysystem;

import com.sep.onlinedeliverysystem.model.Address;
import com.sep.onlinedeliverysystem.model.User;

public final class TestUtil {
    private TestUtil(){

    }

    public static User userBuild1() {
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

    public static User userBuild2() {
        User user = User.builder()
                .id(2L)
                .first_name("Hugh")
                .last_name("Kiggell")
                .email("hugh@kiggmail.com")
                .password("drowssap")
                .role("customer")
                .build();
        return user;
    }

    public static User userBuild3() {
        User user = User.builder()
                .id(3L)
                .first_name("Anthony")
                .last_name("Price")
                .email("anthony@pricemmail.com")
                .password("wordpass")
                .role("customer")
                .build();
        return user;
    }

    public static Address addressBuild1() {
        Address address = Address.builder()
                .id(100L)
                .userId(0L)
                .street("123 Kiggell Road")
                .city("Bristol")
                .postCode("A12 B34")
                .country("Wales")
                .build();
        return address;
    }

    public static Address addressBuild2() {
        Address address = Address.builder()
                .id(200L)
                .userId(0L)
                .street("123 Price Road")
                .city("Bristol")
                .postCode("C12 D34")
                .country("Wales")
                .build();
        return address;
    }

    public static Address addressBuild3() {
        Address address = Address.builder()
                .id(300L)
                .userId(0L)
                .street("123 Li Road")
                .city("Bristol")
                .postCode("E12 F34")
                .country("Wales")
                .build();
        return address;
    }
}
