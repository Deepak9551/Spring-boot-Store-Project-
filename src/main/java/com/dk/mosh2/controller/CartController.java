package com.dk.mosh2.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static java.util.Objects.isNull;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

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
import com.dk.mosh2.service.CartService;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/carts")
public class CartController {

  

	@Autowired
	private CartRepository cartRepository ;
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CartMapper cartMapper;
	
	@Autowired
	private CartService cartService;
  
	
	@PostMapping
	public ResponseEntity<?> createCard(UriComponentsBuilder uriBuilder){
		CartDto createdCart = cartService.createCart();
		System.out.println(createdCart);
		URI uri  = uriBuilder.path("/{id}").buildAndExpand(createdCart.getId()).toUri();

        return new ResponseEntity<>(createdCart, HttpStatus.CREATED);
	}
	
	
	@PostMapping("/{cartId}/items")
	public ResponseEntity<?> addtoCart(
			@RequestBody AddItemToCardRequest request,
			@PathVariable UUID cartId
			){
		
	CartItemsDto responseDto = cartService.addToCart(cartId, request);
		return ResponseEntity.ok(responseDto);
	}
	
	
	@GetMapping("/{cartId}")
	public ResponseEntity<?> getCart( @PathVariable UUID cartId){
		CartDto cartDto = null;
		cartDto = cartService.getCart(cartId);
		return ResponseEntity.ok(cartDto);
	}
	
	@PatchMapping("/{cartId}/items/{productId}")
	public ResponseEntity<?> updateCartItem(@PathVariable UUID cartId ,
			@PathVariable Long productId,
			 @Valid @RequestBody UpdateCardItemDto updateCardItemDto){
		
		
		cartService.updateCartItem(cartId, productId, updateCardItemDto);
		return ResponseEntity.ok("Item update in the cart!!");
		
	}
	
	// Remove a product from Cart 
	
	@DeleteMapping("/{cartId}/product/{productId}")
	public ResponseEntity<?> removeItem(
			@PathVariable UUID cartId,
			@PathVariable Long productId
			
			){
		
		cartService.removeItem(cartId, productId);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{cartId}/items")
	public ResponseEntity<?> clearCart(@PathVariable UUID cartId){
		cartService.clearCart(cartId);
		return ResponseEntity.noContent().build();
	}
}
