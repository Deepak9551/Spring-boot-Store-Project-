package com.dk.mosh2.payment.PaymentGateWay;

import com.dk.mosh2.service.exception.PaymentGateWayException;
import com.dk.mosh2.payment.Webhook.PaymentResult;
import com.dk.mosh2.payment.Webhook.WebHookRequest;
import com.dk.mosh2.enums.OrderStatus;
import com.dk.mosh2.model.Order;
import com.dk.mosh2.model.OrderItem;
import com.dk.mosh2.repo.OrderRepository;
import com.dk.mosh2.payment.checkoutService.CheckOutSession;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

@Service
public class StripePaymentGateWay implements PaymentGateway {

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    private OrderRepository orderRepository;
    public StripePaymentGateWay(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    @Override
    public CheckOutSession paymentGateWay(Order order) {
        try {
            var builder = SessionCreateParams
                    .builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
//            .setSuccessUrl(WebsiteUrl + "/checkout/success?orderID="+ order.getId())
                    .setSuccessUrl("http://localhost:8080/success")
                    .setCancelUrl("http://localhost:8080/cancel")
                    .putMetadata("order_id",order.getId().toString());


            order.getOrderItem()
                    .forEach(item ->

                                createLineItem(item, builder)

);

var checkOutSession = builder.build();

// return the session id to the client
            var session = Session.create(checkOutSession);
            return new CheckOutSession(session.getUrl());
        }
        catch (StripeException e) {
            throw new PaymentGateWayException(e);
        }
    }

    @Override
    public Optional<PaymentResult> parseWebhookEvent(WebHookRequest webHookRequest) {
        try {
            var header = webHookRequest.getHeaders();
            var payload = webHookRequest.getPayload();
            System.out.println("webhookSecret = " + webhookSecret);

            // Method ke andar ye logic use karein:
            String signature = header.get("stripe-signature"); // Lower-case try karein
            if (signature == null) {
                signature = header.get("Stripe-Signature"); // Phir Camel-Case try karein
            }

            if (signature == null || signature.isEmpty()) {
                throw new SignatureVerificationException("Missing Stripe-Signature header", "");
            }
            var event  = Webhook.constructEvent(
                    payload, signature, webhookSecret
            );


            var orderId = extractOrderId(event);
            switch (event.getType()){
                case "payment_intent.succeeded":
                    // Handle successful payment intent
                    System.out.println("PaymentIntent was successful!");



                    return Optional.of(new PaymentResult(BigInteger.valueOf(orderId), OrderStatus.PAID));


                case "payment_intent.payment_failed":
                    // Handle failed payment intent
                    System.out.println("PaymentIntent failed.");
                    return Optional.of(new PaymentResult(BigInteger.valueOf(orderId), OrderStatus.PAID));

            }

        } catch (SignatureVerificationException  e) {
            System.out.println(e.getMessage());
           throw new PaymentGateWayException("Inalid signature found");
        }

        return Optional.empty();
    }

    public Long extractOrderId(Event event){
        var stripeObject = event.getDataObjectDeserializer().getObject()
                .orElseThrow( ()-> new PaymentGateWayException("Unable to deserialize event object"));
        String orderId = null;

        // 2. Type-safe checking and casting
        if (stripeObject instanceof PaymentIntent paymentIntent) {
            orderId = paymentIntent.getMetadata().get("order_id");
        }
        else if (stripeObject instanceof com.stripe.model.checkout.Session session) {
            orderId = session.getMetadata().get("order_id");
        }
        else if (stripeObject instanceof Charge charge) {
            orderId = charge.getMetadata().get("order_id");
        }
        if (orderId == null) {
            throw  new PaymentGateWayException("Order ID not found in payment intent metadata");
        }
        return  Long.valueOf(orderId);
    }

    private static void createLineItem(OrderItem item, SessionCreateParams.Builder builder) {
        var listItem = SessionCreateParams.LineItem.builder()
                .setQuantity((long) item.getQuantity())
                .setPriceData(
                        getPriceData(item)
                ).build();

        builder.addLineItem(listItem);
    }

    private static SessionCreateParams.LineItem.PriceData getPriceData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("inr")
                .setUnitAmountDecimal(item.getUnitPrice().multiply(
                        BigDecimal.valueOf(100)
                ))
                .setProductData(
                        getProductData(item)
                )
                .build();
    }

    private static SessionCreateParams.LineItem.PriceData.ProductData getProductData(OrderItem item) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(item.getProduct().getName()).build();
    }
}
