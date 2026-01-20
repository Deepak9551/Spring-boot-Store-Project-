package com.dk.mosh2.model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * The persistent class for the order_items database table.
 * 
 */
@Entity
@Table(name="order_items")

public class OrderItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private BigInteger id;

	private int quantity;

	@Column(name="total_price")
	private BigDecimal totalPrice;

	@Column(name="unit_price")
	private BigDecimal unitPrice;

	//bi-directional one-to-one association to Order
	@ManyToOne
	@JoinColumn(name="order_id", referencedColumnName="id")
	private Order order;

	//bi-directional one-to-one association to Product
	@OneToOne
	@JoinColumn(name="product_id", referencedColumnName="id")
	private Product product;

	public OrderItem() {
	}

	public OrderItem(Order order, Product product, int quantity) {
		
		// TODO Auto-generated constructor stub
		this.order = order;
		this.product = product;
		this.quantity = quantity;
		this.unitPrice = product.getPrice();
		this.totalPrice =  unitPrice.multiply(BigDecimal.valueOf(quantity));
	}

	public BigInteger getId() {
		return this.id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getTotalPrice() {
		return this.totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public BigDecimal getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Order getOrder() {
		return this.order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}