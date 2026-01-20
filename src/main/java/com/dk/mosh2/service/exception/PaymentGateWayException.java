package com.dk.mosh2.service.exception;

import com.stripe.exception.StripeException;

public class PaymentGateWayException extends RuntimeException {
    public PaymentGateWayException(StripeException e) {
    super(e);
    }
    public PaymentGateWayException(String e) {
        super(e);
    }
}
