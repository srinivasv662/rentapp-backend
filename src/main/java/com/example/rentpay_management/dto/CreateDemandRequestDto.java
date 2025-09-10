package com.example.rentpay_management.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateDemandRequestDto {

    @NotNull(message = "Vendor ID is required")
    private Long vendorId;

//    @NotBlank
//    private String tenantEmail;

    @NotNull(message = "Property ID is required")
    private String propertyId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Due Date is required")
    @FutureOrPresent(message = "Due date cannot be in the past")
    private LocalDate dueDate;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;
}
