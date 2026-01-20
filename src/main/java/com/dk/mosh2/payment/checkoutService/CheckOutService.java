package com.dk.mosh2.payment.checkoutService;

import com.dk.mosh2.payment.Webhook.WebHookRequest;
import com.dk.mosh2.enums.OrderStatus;
import com.dk.mosh2.payment.PaymentGateWay.PaymentGateway;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dk.mosh2.exceptions.EmptyResourceException;
import com.dk.mosh2.exceptions.ResourceNotFoundException;
import com.dk.mosh2.payment.CheckOutRequest;
import com.dk.mosh2.payment.CheckOutResponse;
import com.dk.mosh2.model.Cart;
import com.dk.mosh2.model.Order;
import com.dk.mosh2.repo.CartRepository;
import com.dk.mosh2.repo.OrderRepository;
import com.dk.mosh2.service.CartService;
import com.dk.mosh2.service.jwt.AuthService;

@Service

public class CheckOutService {

	
	private OrderRepository orderRepository;
	private CartRepository cartRepository;
	
	private AuthService authService;
	
	private CartService cartService;

	private PaymentGateway paymentGateway;


	@Value("@{website.url}")
	private String WebsiteUrl;



	public CheckOutService(OrderRepository orderRepository, CartRepository cartRepository, AuthService authService,
			CartService cartService , PaymentGateway paymentGateway) {
		super();
		this.orderRepository = orderRepository;
		this.paymentGateway = paymentGateway;
		this.cartRepository = cartRepository;
		this.authService = authService;
		this.cartService = cartService;
	}


	@Transactional
	public CheckOutResponse checkOut(CheckOutRequest checkOutRequest)  {
		Cart cart = cartRepository.findById
				(checkOutRequest.getCartId()).
				orElseThrow(()-> new ResourceNotFoundException("card id not found"));
		if(cart.isEmpty()) throw new EmptyResourceException("cart is empty ");
		
		
		
		Order order = Order.placeOrderFromCart(cart, authService.currentUser());


		// save the order in the data base
		Order placedOrder = orderRepository.save(order);
		// create a checkout Session

		var checkOutSession =  paymentGateway.paymentGateWay(placedOrder);

            // clear the cart
            cartService.clearCart(cart.getId());

            return new CheckOutResponse(order.getId(),checkOutSession.getSessionUrl());

    }

	public void handleWebhookEvent(WebHookRequest webHookRequest) {
		// Implementation for handling webhook events
		paymentGateway.parseWebhookEvent(webHookRequest)
				.ifPresent(
						(paymentResult)->{
							var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
							order.setStatus(OrderStatus.PAID);
							orderRepository.save(order);
						}
				);

	}
}
