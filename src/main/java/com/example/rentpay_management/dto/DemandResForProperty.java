package com.example.rentpay_management.dto;

import com.example.rentpay_management.model.Demand;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemandResForProperty {

    private Long demandId;

    private BigDecimal amount;

    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Demand.Status status;

    private LocalDateTime createdAt;

    private String description;

}
