package com.dk.mosh2.payment;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckOutRequest {

	@NotNull(message = "cart id must be present ")
	
	private UUID cartId;
}
