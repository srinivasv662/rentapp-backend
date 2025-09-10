package com.example.rentpay_management.controller;

import com.example.rentpay_management.model.Demand;
import com.example.rentpay_management.model.Payment;
import com.example.rentpay_management.repository.DemandRepository;
import com.example.rentpay_management.repository.PaymentRepository;
import com.example.rentpay_management.serviceimpl.RentServiceImpl;
import com.example.rentpay_management.services.RentService;
import com.example.rentpay_management.services.WebhookService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private static final Logger log = LoggerFactory.getLogger(WebhookController.class);

    private final WebhookService webhookService;

    @PostMapping("/cashfree")
    public ResponseEntity<?> handleWebhook(@RequestBody String payload,
                                @RequestHeader(value = "x-webhook-signature", required = false) String signature,
                                @RequestHeader(value = "x-webhook-timestamp", required = false) String timeStamp) throws Exception {
        try {
            log.info("Webhook Payload: {}", payload);
            log.info("Signature: {}", signature);
            log.info("TimeStamp: {}", timeStamp);

            String status = webhookService.webhookHandler(payload, signature, timeStamp);

            switch(status.toUpperCase()) {
                case "WEBHOOK_TEST":
                    return ResponseEntity.ok(
                            Map.of("message", "Webhook test success", "status", status)
                    );
                case "SUCCESS":
                    return ResponseEntity.ok(
                            Map.of("message", "Payment successful", "status", status)
                    );
                case "FAILED":
                    return ResponseEntity.badRequest().body(
                            Map.of("message", "Payment failed", "status", status)
                    );
                default:
                    return ResponseEntity.status(400).body(
                            Map.of("message", "Unknown webhook status", "status", status)
                    );
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("message", "Internal server error", "error", e.getMessage())
            );
        }
    }

}
