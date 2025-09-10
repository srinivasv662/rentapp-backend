package com.example.rentpay_management.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

//@Setter
//@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "demands")
public class Demand{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User vendor;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Status status;

    private LocalDateTime createdAt;

    private String description;

    @Column(nullable = true, unique = true)
    private String cashfreeOrderId;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

//    @Column(unique = true)
    private String propertyCode;

    // Not mandatory but usually useful
    @OneToMany(mappedBy = "demand", cascade = CascadeType.ALL)
    private List<Payment> payments;

    public enum Status { PENDING, PAID, OVERDUE, FAILED }

}
