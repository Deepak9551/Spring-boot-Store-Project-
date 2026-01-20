package com.dk.mosh2.dto;

import lombok.Data;

@Data
public class CreateProductRequest {
	private String name;
	private String description;
	private Double price;
	private String categoryId;
}
