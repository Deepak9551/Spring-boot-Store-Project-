package com.dk.mosh2.payment.PaymentGateWay;

import com.dk.mosh2.payment.Webhook.PaymentResult;
import com.dk.mosh2.payment.Webhook.WebHookRequest;
import com.dk.mosh2.model.Order;
import com.dk.mosh2.payment.checkoutService.CheckOutSession;

import java.util.Optional;

public interface PaymentGateway {
CheckOutSession paymentGateWay(Order order);
Optional<PaymentResult> parseWebhookEvent(WebHookRequest webHookRequest) ;
}
