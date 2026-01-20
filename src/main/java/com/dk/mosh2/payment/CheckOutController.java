package com.dk.mosh2.payment;

import java.util.Map;

import com.dk.mosh2.payment.Webhook.WebHookRequest;
import com.dk.mosh2.repo.OrderRepository;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dk.mosh2.payment.checkoutService.CheckOutService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/checkout")
@Data
public class CheckOutController {


	private CheckOutService checkOutService;




	private OrderRepository orderRepository;

	public CheckOutController(CheckOutService checkOutService, OrderRepository orderRepository) {
		this.checkOutService = checkOutService;
		this.orderRepository = orderRepository;
	}

	@PostMapping("/checkout")
	public ResponseEntity<?> checkout(
	@Valid @RequestBody CheckOutRequest checkOutRequest){
        CheckOutResponse checkOutResponse = null;

            checkOutResponse = checkOutService.checkOut(checkOutRequest);

        return ResponseEntity.ok(checkOutResponse);
	}
@PostMapping("/webhook")
	public ResponseEntity<?> webhook(
@RequestHeader Map<String,String> headers

	, @RequestBody String payload
	)  {



	checkOutService.handleWebhookEvent(new WebHookRequest(payload,headers));
        return ResponseEntity.status(HttpStatus.OK).build();
	}
}
