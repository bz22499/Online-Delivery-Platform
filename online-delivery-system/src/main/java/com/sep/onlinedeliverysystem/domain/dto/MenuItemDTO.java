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
public class MenuItemDTO {
    private Long id;
    private String name;
    private String description;
    private float price;
    private Vendor vendor;
}
