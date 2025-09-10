//package com.example.rentpay_management.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//@Setter
//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@Entity
//@Table(name = "vendor_profiles")
//public class VendorProfile {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @OneToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    private String shopName;
//    private String contactNumber;
//    private String area;
//    private String ward;
//    private String property;
//}
