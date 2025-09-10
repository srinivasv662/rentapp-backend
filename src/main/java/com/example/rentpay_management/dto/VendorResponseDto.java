package com.example.rentpay_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VendorResponseDto {

    private Long vendorUserId;
    private String name;
    private String email;
    private String role;
    private String shopName;
    private String contactNumber;
    private String area;
    private String ward;
    private String property;

}
