package com.dk.mosh2.model;

import java.io.Serializable;
import jakarta.persistence.*;

import java.util.List;


/**
 * The persistent class for the categories database table.
 * 
 */
@Entity
@Table(name="categories")

public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private byte id;

	private String name;

	//bi-directional many-to-one association to Product
	@OneToMany(mappedBy="category")
	private List<Product> products;

	public Category() {
	}

	public byte getId() {
		return this.id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Product> getProducts() {
		return this.products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public Product addProduct(Product product) {
		getProducts().add(product);
		product.setCategory(this);

		return product;
	}

	public Product removeProduct(Product product) {
		getProducts().remove(product);
		product.setCategory(null);

		return product;
	}

}