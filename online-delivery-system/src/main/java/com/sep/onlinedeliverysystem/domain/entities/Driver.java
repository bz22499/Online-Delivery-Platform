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
@Table(name = "drivers")
public class Driver implements Serializable {
    @Id
    private String email;
    private String name;
    private String password;
    private float rating;

    public DriverDetailsEntity toDriverDetails() {
        return new DriverDetailsEntity(this);
    }

}