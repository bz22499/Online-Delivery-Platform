package com.sep.onlinedeliverysystem.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log
public class Address {
    private Long id;
    private Long userId;
    private String street;
    private String city;
    private String postCode;
    private String country;
}
