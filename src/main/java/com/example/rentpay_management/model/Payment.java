package com.example.rentpay_management.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payments")
public class Payment extends AbstractTimestampEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Demand demand;

//    @Column(nullable = false, precision = 12, scale = 2)
//    private BigDecimal paidAmount;
    @Column(nullable = false)
    private Double paymentAmount;

    @Column(nullable = false)
    private LocalDateTime paidAt;

    private String paymentId;
    private String paymentMode;
    private String paymentStatus;
}
