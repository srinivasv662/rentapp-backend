package com.example.rentpay_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendorProfileResponseDto {

    private Long id;
    private String name;
    private String email;
    private String contact;

    // Property details
    private String propertyId;
    private String shopName;
    private String area;
    private String ward;

}
