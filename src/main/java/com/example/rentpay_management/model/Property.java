package com.example.rentpay_management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property extends AbstractTimestampEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true)
    private String propertyId;
    private String shopName;
    private String area;
    private String ward;

    @OneToMany(mappedBy = "property")
    private List<Demand> demands;

    @OneToMany(mappedBy = "property")
    private List<User> vendors;

}
