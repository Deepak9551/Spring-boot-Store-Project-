package com.dk.mosh2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.PatchExchange;
import org.springframework.web.util.UriComponentsBuilder;

import com.dk.mosh2.dto.CreateProductRequest;
import com.dk.mosh2.mapper.ProductMapper;
import com.dk.mosh2.model.Product;
import com.dk.mosh2.repo.ProductRepository;

@RestController
@RequestMapping("/v1/products")
public class ProductController {
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private ProductRepository productRepository;
	
	@PostMapping("/add")
	public ResponseEntity<?> addProduct(
			@RequestBody CreateProductRequest createProductRequest , 
			UriComponentsBuilder uriBuilder
			){
	var product = 	productMapper.toEntity(createProductRequest);
	try {
		
		Product addedProduct = productRepository.save(product);
		var uri = uriBuilder.path("/{id}").buildAndExpand(addedProduct.getId()).toUri();
		return ResponseEntity.ok(addedProduct).created(uri).build();
	}
	catch (Exception e) {
		
	
		System.out.println(e.getMessage());
			return ResponseEntity.badRequest().build();
		
		
	}
	}
	@GetMapping("/{id}")
	public ResponseEntity<?> getProduct(@PathVariable Long id){

	
	try {
		
		var product = 	productRepository.findById(id);
		if(product == null) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(product);
	
	}
	catch (Exception e) {
		// TODO: handle exception
	
		System.out.println(e.getMessage());
			return ResponseEntity.badRequest().build();
		
		
	}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable Long id
			,CreateProductRequest createProductRequest
			){

	
	try {
		
		var product = 	productRepository.findById(id).orElse(null);
		if(product == null) {
			return ResponseEntity.notFound().build();
		}
		productMapper.update(createProductRequest,product);
		return ResponseEntity.ok(product);
	
	}
	catch (Exception e) {
		// TODO: handle exception
	
		System.out.println(e.getMessage());
			return ResponseEntity.badRequest().build();
		
		
	}
	}
}
