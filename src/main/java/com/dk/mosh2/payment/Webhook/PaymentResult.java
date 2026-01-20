package com.dk.mosh2.payment.Webhook;

import com.dk.mosh2.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class PaymentResult {
    private BigInteger orderId;
    private OrderStatus status;
}
