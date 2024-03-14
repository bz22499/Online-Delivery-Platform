package com.sep.onlinedeliverysystem.domain.dto;


import com.sep.onlinedeliverysystem.domain.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAddressDTO {
    private Long id;
    private User user;
    private String street;
    private String city;
    private String postCode;
    private String country;
}
