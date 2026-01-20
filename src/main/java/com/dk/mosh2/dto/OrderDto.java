package com.dk.mosh2.dto;

import com.dk.mosh2.model.Order;
import com.dk.mosh2.model.Product;
import lombok.Data;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
@Data
public class OrderDto {

    private BigInteger id;

    private String  status;


    private LocalDateTime createdAt;


    private List<OrderItemDto> items;



    private BigDecimal totalPrice;
}
