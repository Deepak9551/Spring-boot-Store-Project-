package com.dk.mosh2.payment.Webhook;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class WebHookRequest {

    private String payload;
    private Map<String,String> headers;
}
