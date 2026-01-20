package com.dk.mosh2.payment;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "stripe")
@Component
@Data
public class StripeConfig {

    private String secretKey;

    @PostConstruct
    public void init(){
        System.out.println("Stripe secret key: " + secretKey);
        Stripe.apiKey= secretKey;
    }
}
