package com.example.rentpay_management.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitiatePaymentRequestDto {

    @NotNull(message = "Vendor ID is required")
    private Long vendorId;

    @NotNull(message = "Demand ID is required")
    private Long demandId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private Double amount;
}
