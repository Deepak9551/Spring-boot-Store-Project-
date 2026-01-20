package com.dk.mosh2.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.dk.mosh2.dto.CreateProductRequest;
import com.dk.mosh2.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
	
	Product toEntity(CreateProductRequest createProductrequest);
	
	@Mapping(target = "id",ignore = true)
	void update(CreateProductRequest productRequest , @MappingTarget Product p);

}
