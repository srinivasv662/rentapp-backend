package com.example.rentpay_management.controller;

import com.cashfree.pg.ApiResponse;
import com.cashfree.pg.model.OrderEntity;
import com.example.rentpay_management.dto.InitiatePaymentRequestDto;
import com.example.rentpay_management.services.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
//@PreAuthorize("hasRole('VENDOR')")
public class PaymentController {

    private final PaymentService paymentServiceImpl;

    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@Valid @RequestBody InitiatePaymentRequestDto request) {

        try{
//            Long vendorId = Long.parseLong(request.get("vendorId").toString());
//            Long demandId = Long.parseLong(request.get("demandId").toString());
//            Double amount = Double.parseDouble(request.get("amount").toString());

            Long vendorId = request.getVendorId();
            Long demandId = request.getDemandId();
            Double amount = request.getAmount();
            ApiResponse<OrderEntity> response = paymentServiceImpl.createOrder(vendorId, demandId, amount);

            return ResponseEntity.ok(response.getData());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Something went wrong", "details", e.getMessage()));
        }



    }

}
