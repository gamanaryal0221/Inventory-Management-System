package com.ivs.product_service.domain.external;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {

    private Long id;
    private String productId;
    private Integer totalQuantity;
    private Integer reservedQuantity;
}