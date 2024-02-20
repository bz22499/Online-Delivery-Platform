package com.sep.onlinedeliverysystem.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverDTO {
    private String email;
    private String name;
    private String password;
    private float rating;
}
