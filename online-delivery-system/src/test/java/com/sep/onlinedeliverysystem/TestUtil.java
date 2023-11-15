package com.sep.onlinedeliverysystem;

import com.sep.onlinedeliverysystem.entity.Address;
import com.sep.onlinedeliverysystem.entity.User;

public final class TestUtil {
    private TestUtil(){

    }

    public static User userBuild1() {
        User user = User.builder()
            .email("luke@trottmail.com")
            .firstName("Luke")
            .lastName("Trott")
            .password("password")
            .role("customer")
            .build();
        return user;
    }

    public static User userBuild2() {
        User user = User.builder()
                .email("hugh@kiggmail.com")
                .firstName("Hugh")
                .lastName("Kiggell")
                .password("drowssap")
                .role("customer")
                .build();
        return user;
    }

    public static User userBuild3() {
        User user = User.builder()
                .email("anthony@pricemmail.com")
                .firstName("Anthony")
                .lastName("Price")
                .password("wordpass")
                .role("customer")
                .build();
        return user;
    }

    public static Address addressBuild1(final User user) {
        Address address = Address.builder()
                .id(100L)
                .user(user)
                .street("123 Kiggell Road")
                .city("Bristol")
                .postCode("A12 B34")
                .country("Wales")
                .build();
        return address;
    }

    public static Address addressBuild2(final User user) {
        Address address = Address.builder()
                .id(200L)
                .user(user)
                .street("123 Price Road")
                .city("Bristol")
                .postCode("C12 D34")
                .country("Wales")
                .build();
        return address;
    }

    public static Address addressBuild3(final User user) {
        Address address = Address.builder()
                .id(300L)
                .user(user)
                .street("123 Li Road")
                .city("Bristol")
                .postCode("E12 F34")
                .country("Wales")
                .build();
        return address;
    }
}
