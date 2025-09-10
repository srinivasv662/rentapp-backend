package com.example.rentpay_management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reconciliation extends AbstractTimestampEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long vendorId;

    private String vendorName;

    private Long demandId;

    private String propertyCode;

    private Double paymentAmount;

    private LocalDateTime paidAt;

    @Column(nullable = false, unique = true)
    private String paymentId;

    private String paymentMode;

    private String paymentStatus;

    private Double orderAmount;
    private Double serviceCharge;
    private Double serviceTax;
    private Double settlementAmount;
}
