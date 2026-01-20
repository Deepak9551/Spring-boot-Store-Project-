package com.dk.mosh2.payment;

import java.math.BigInteger;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CheckOutResponse {

	private BigInteger orderId;

	private String checkoutUrl;

	public CheckOutResponse(BigInteger orderId, String checkoutUrl) {
		this.orderId = orderId;
		this.checkoutUrl = checkoutUrl;
	}
}
