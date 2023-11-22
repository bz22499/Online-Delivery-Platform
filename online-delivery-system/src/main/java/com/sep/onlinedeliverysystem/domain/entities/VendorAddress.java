package com.sep.onlinedeliverysystem.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "vendor_addresses")
public class VendorAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "vendor_address_id")
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vendor_email", nullable = false)
    private Vendor vendor;
    private String street;
    private String city;
    private String postCode;
    private String country;
}
