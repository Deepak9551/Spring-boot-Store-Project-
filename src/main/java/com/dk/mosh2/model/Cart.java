package com.dk.mosh2.model;

import static java.util.Objects.isNull;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.*;

import java.util.Date;
import java.util.LinkedHashSet;

import java.util.Set;
import java.util.UUID;





/**
 * The persistent class for the carts database table.
 * 
 */
@Entity
@Table(name="carts")

public class Cart implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy =  GenerationType.UUID)

	private UUID id;


	@Column(name="create_date" , insertable = false , updatable = false)
	private Date createDate;

	//bi-directional many-to-one association to CartItem
	@OneToMany(mappedBy="cart" , orphanRemoval = true ,cascade = CascadeType.ALL ,fetch = FetchType.EAGER)
	private Set<CartItem> cartItems = new LinkedHashSet<>();

	public Cart() {
	}

	
	public UUID getId() {
		return id;
	}


	public void setId(UUID id) {
		this.id = id;
	}


	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Set<CartItem> getCartItems() {
		return this.cartItems;
	}

	public void setCartItems(Set<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	public CartItem addCartItem(CartItem cartItem) {
		getCartItems().add(cartItem);
		cartItem.setCart(this);

		return cartItem;
	}

	public CartItem removeCartItem(CartItem cartItem) {
		getCartItems().remove(cartItem);
		cartItem.setCart(null);

		return cartItem;
	}
	
	public BigDecimal getTotalPrice() {
		return this.cartItems.stream()
				.map(cartItem -> cartItem.getTotalPrice())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	public CartItem getItem(Long productId) {
		return this.getCartItems().stream()
		.filter(cardItem -> cardItem.getProduct().getId().equals(productId))
		.findFirst().orElse(null);
		
	} 

	public CartItem addItemToCart(Product product ) {
		CartItem cartItem  = getItem(product.getId());
		if(!isNull(cartItem)) {
			cartItem.setQuantity(cartItem.getQuantity() + 1);
			
		}
		else {
		
		cartItem = new CartItem();
		
		cartItem.setCart(this);
		
		cartItem.setProduct(product);
		
		
		
		cartItem.setQuantity(1);
		
		cartItems.add(cartItem);
		}
		return cartItem;
	}
	
	public boolean removeItem(Long productId) {
		
		
		CartItem cartItem = getItem(productId);
		if(!isNull(cartItem)) {
			// this line makes the cartItem to be in orphan behaviour
			// when save whole cart object  
			// hibernate auto remove cartItem remove because its column is disconnected
			// with parent ref
			cartItem.setCart(null);
			
			// for balance make the fetch whole cart 
			//object internal ref of cartItem to be null
			cartItems.remove(cartItem);
			return true;
		}else {
			return false;
		}
	}
	
	public boolean isEmpty() {
		return this.cartItems.isEmpty();
	}
	
	public void clearCart() {
		cartItems.clear();
	}

}