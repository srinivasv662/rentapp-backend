package com.example.rentpay_management.services;

import org.springframework.http.ResponseEntity;

public interface WebhookService {

    public String webhookHandler(String payload, String signature, String timeStamp) throws Exception;

}
