package com.sep.onlinedeliverysystem;

import com.sep.onlinedeliverysystem.domain.dto.AddressDTO;
import com.sep.onlinedeliverysystem.domain.dto.UserDTO;
import com.sep.onlinedeliverysystem.domain.entities.AddressEntity;
import com.sep.onlinedeliverysystem.domain.entities.UserEntity;

public final class TestUtil {
    private TestUtil(){

    }

    public static UserEntity userBuild1() {
        UserEntity userEntity = UserEntity.builder()
            .email("luke@trottmail.com")
            .firstName("Luke")
            .lastName("Trott")
            .password("password")
            .role("customer")
            .build();
        return userEntity;
    }

    public static UserEntity userBuild2() {
        UserEntity userEntity = UserEntity.builder()
                .email("hugh@kiggmail.com")
                .firstName("Hugh")
                .lastName("Kiggell")
                .password("drowssap")
                .role("customer")
                .build();
        return userEntity;
    }

    public static UserEntity userBuild3() {
        UserEntity userEntity = UserEntity.builder()
                .email("anthony@pricemmail.com")
                .firstName("Anthony")
                .lastName("Price")
                .password("wordpass")
                .role("customer")
                .build();
        return userEntity;
    }

    public static AddressEntity addressBuild1(final UserEntity userEntity) {
        AddressEntity addressEntity = AddressEntity.builder()
                .id(100L)
                .userEntity(userEntity)
                .street("123 Kiggell Road")
                .city("Bristol")
                .postCode("A12 B34")
                .country("Wales")
                .build();
        return addressEntity;
    }

    public static AddressDTO addressDTOCreate1(final UserDTO userDTO) {
        AddressDTO addressDTO = AddressDTO.builder()
                .id(101L)
                .userDTO(userDTO)
                .street("123 Kiggell Road")
                .city("Bristol")
                .postCode("A12 B34")
                .country("Wales")
                .build();
        return addressDTO;
    }

    public static AddressEntity addressBuild2(final UserEntity userEntity) {
        AddressEntity addressEntity = AddressEntity.builder()
                .id(200L)
                .userEntity(userEntity)
                .street("123 Price Road")
                .city("Bristol")
                .postCode("C12 D34")
                .country("Wales")
                .build();
        return addressEntity;
    }

    public static AddressEntity addressBuild3(final UserEntity userEntity) {
        AddressEntity addressEntity = AddressEntity.builder()
                .id(300L)
                .userEntity(userEntity)
                .street("123 Li Road")
                .city("Bristol")
                .postCode("E12 F34")
                .country("Wales")
                .build();
        return addressEntity;
    }
}
