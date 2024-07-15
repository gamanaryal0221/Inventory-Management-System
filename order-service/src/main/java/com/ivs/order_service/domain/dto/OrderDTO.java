package com.ivs.order_service.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private UserDTO user;
    private ProductDTO product;
    private Long orderId;
    private int quantity;
    private double totalPrice;
}
