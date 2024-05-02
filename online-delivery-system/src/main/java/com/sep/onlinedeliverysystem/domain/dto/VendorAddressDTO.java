package com.sep.onlinedeliverysystem.domain.dto;

import com.sep.onlinedeliverysystem.domain.entities.Vendor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VendorAddressDTO {
    private Long id;
    private Vendor vendor;
    private String street;
    private String city;
    private String postCode;
    private String country;
}
