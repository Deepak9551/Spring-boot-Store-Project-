package com.dk.mosh2.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class CartItemsDto {


	

 private CardProductDto product ;
 private int quantity;
 private BigDecimal totalPrice ;
}
