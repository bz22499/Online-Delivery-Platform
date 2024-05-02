package com.sep.onlinedeliverysystem.domain.dto;

import com.sep.onlinedeliverysystem.domain.entities.Basket;
import com.sep.onlinedeliverysystem.domain.entities.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasketItemDTO {
    private Long id;
    private Basket basket;
    private MenuItem menuItem;
    private int quantity;
}
