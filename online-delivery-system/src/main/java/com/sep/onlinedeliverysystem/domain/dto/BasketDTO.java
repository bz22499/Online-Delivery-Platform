package com.sep.onlinedeliverysystem.domain.dto;

import com.sep.onlinedeliverysystem.domain.entities.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasketDTO {
    private Long id;
    private Order order;
}
