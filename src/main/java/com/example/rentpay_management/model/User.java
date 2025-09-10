package com.example.rentpay_management.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User extends AbstractTimestampEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String contact;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column
    private LocalDate passwordExpiry;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    private String propertyCode;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL)
    private List<Demand> demands;

    public enum Role {SUPER_ADMIN, VENDOR}
}
