package com.dk.mosh2.model;

import java.io.Serializable;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * The persistent class for the products database table.
 * 
 */
@Entity
@Table(name="products")

public class Product implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	private BigDecimal price;

	//bi-directional many-to-one association to Category
	@ManyToOne(cascade = CascadeType.ALL)
	private Category category;

	public Product() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Category getCategory() {
		return this.category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

}