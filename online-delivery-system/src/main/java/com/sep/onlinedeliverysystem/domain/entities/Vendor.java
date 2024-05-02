package com.sep.onlinedeliverysystem.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "vendors")
public class Vendor implements Serializable {
    @Id
    private String email;
    private String name;
    private String password;
    private String description;
    private float rating;
    private String imageUrl;

    public VendorDetailsEntity toVendorDetails() {
        return new VendorDetailsEntity(this);
    }
}