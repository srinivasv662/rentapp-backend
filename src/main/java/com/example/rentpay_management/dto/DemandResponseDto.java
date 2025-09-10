package com.example.rentpay_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DemandResponseDto {
    private Long demandId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDate dueDate;
    private String description;
    private Long vendorId;
    private String vendorName;
    private String propertyCode;
}
