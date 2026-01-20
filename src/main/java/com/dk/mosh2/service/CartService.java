package com.dk.mosh2.service;

import static java.util.Objects.isNull;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dk.mosh2.exceptions.ResourceNotFoundException;
import com.dk.mosh2.dto.AddItemToCardRequest;
import com.dk.mosh2.dto.CartDto;
import com.dk.mosh2.dto.CartItemsDto;
import com.dk.mosh2.dto.UpdateCardItemDto;
import com.dk.mosh2.mapper.CartMapper;
import com.dk.mosh2.model.Cart;
import com.dk.mosh2.model.CartItem;
import com.dk.mosh2.model.Product;
import com.dk.mosh2.repo.CartRepository;
import com.dk.mosh2.repo.ProductRepository;

@Service
public class CartService {

	@Autowired
	private CartMapper cartMapper;
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	public CartDto createCart() {
		var cart = new Cart();
		Cart createCart = cartRepository.save(cart);
		return cartMapper.toCartDto(createCart);
	}
	
	public CartItemsDto addToCart(UUID cartId ,AddItemToCardRequest request) {
		var cart = 	cartRepository.findById(cartId).orElse(null);
		if(isNull(cart)) {
			throw new ResourceNotFoundException("cart not found");
		}
		var product = 	productRepository.findById(request.getProductId()).orElse(null);
		if(isNull(product)) {
			 throw new ResourceNotFoundException("product not found");
		}
		CartItem cartItem = cart.addItemToCart(product); 
		CartItemsDto responseDto = cartMapper.toDto(cartItem);
		cartRepository.save(cart);
		return responseDto;
	}
	
	public CartDto getCart(UUID cartId) {
		CartDto cartDto = null;
		try {
			Cart cart = cartRepository.findById(cartId).orElse(null);
			// if cart is not present then return bad Request
			if(isNull(cart)) new ResourceNotFoundException("cart not found");
			
			cartDto = cartMapper.toCartDto(cart);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return cartDto;
		
	}
	
	public void updateCartItem(UUID cartId, Long productId , UpdateCardItemDto updateCardItemDto) {
		try {
			Cart cart = cartRepository.findById(cartId).orElse(null);
			if(isNull(cart)) throw new ResourceNotFoundException("cart not found");
			
			Product product = productRepository.findById(productId).orElse(null);
			if(isNull(product)) throw new ResourceNotFoundException("product not found");
			CartItem cartItem = cart.getItem(productId);
			
			cartItem.setQuantity(updateCardItemDto.getQuantity());
			
			cartRepository.save(cart);
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
	}
	
	public void removeItem(UUID cartId,Long productId) {
		var cart = cartRepository.findById(cartId).orElse(null);
		// IF Cart not found return < NOT present > MAP
		if(isNull(cart)) throw new ResourceNotFoundException("cart not found");
		
		
		boolean removeStatus = cart.removeItem(productId);
		if(!removeStatus) {
			throw new ResourceNotFoundException("product not found");
		}
		cartRepository.save(cart);
	}
	
	public void clearCart(UUID cartId) {
		
		Cart cart = cartRepository.findById(cartId).orElse(null);
		if(isNull(cart)) throw new ResourceNotFoundException("cart not found");
		cart.clearCart();
		cartRepository.save(cart);
	}
}
