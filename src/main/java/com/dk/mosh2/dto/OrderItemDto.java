package com.dk.mosh2.dto;


import lombok.Data;

import java.math.BigDecimal;
@Data
public class OrderItemDto {

    private OrderProductDto product;
    private int quantity;
    private BigDecimal totalPrice;
}
