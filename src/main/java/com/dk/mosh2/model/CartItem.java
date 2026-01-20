package com.dk.mosh2.model;

import java.io.Serializable;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;


/**
 * The persistent class for the cart_items database table.
 * 
 */
@Entity
@Table(name="cart_items")

public class CartItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy =  GenerationType.UUID)
	private UUID id;

	@JoinColumn(name="product_id")
	@OneToOne
	private Product product;

	private Integer quantity;

	//bi-directional many-to-one association to Cart
	@ManyToOne
	private Cart cart;

	public CartItem() {
	}

	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
	}



	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public int getQuantity() {
		return this.quantity;
	}


	public Cart getCart() {
		return this.cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}
	
	public BigDecimal getTotalPrice() {
		return this.getProduct().getPrice().multiply(BigDecimal.valueOf(quantity));
	}

}