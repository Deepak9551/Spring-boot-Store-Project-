package com.dk.mosh2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.dk.mosh2.dto.AddItemToCardRequest;
import com.dk.mosh2.dto.CartDto;
import com.dk.mosh2.dto.CartItemsDto;
import com.dk.mosh2.model.Cart;
import com.dk.mosh2.model.CartItem;

@Mapper(componentModel = "spring")
public interface CartMapper {

	Cart toEntity(CartDto cartDto);
	
	CartItem toEntity(AddItemToCardRequest addItemToCardRequest);
	
	@Mapping(target = "totalPrice",expression = "java(cartItem.getTotalPrice())")
	CartItemsDto toDto(CartItem cartItem);
	
	@Mapping(target = "items" , source = "cartItems")
	@Mapping(target = "totalPrice" , expression = "java(cart.getTotalPrice())")
	CartDto toCartDto(Cart cart);
}
