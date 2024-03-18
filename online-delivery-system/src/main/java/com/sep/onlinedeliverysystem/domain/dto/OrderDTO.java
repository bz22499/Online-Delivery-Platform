package com.sep.onlinedeliverysystem.domain.dto;

import com.sep.onlinedeliverysystem.domain.entities.UserAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private UserAddress userAddress;
}
