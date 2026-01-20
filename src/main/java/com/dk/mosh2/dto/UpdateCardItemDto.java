package com.dk.mosh2.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCardItemDto {
	
	@NotNull(message = "quantity must be present")
	@Min(value = 1  , message = "quatity must not be less than 1")
	@Max(value = 1000  , message = "quatity must  be greater than 1000")
	private Integer quantity;

}
