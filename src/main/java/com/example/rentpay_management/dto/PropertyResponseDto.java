package com.example.rentpay_management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyResponseDto {

    private String propertyId;
    private String shopName;
    private String area;
    private String ward;

    private List<UserRepsonseDto> userRepsonseDtos;

}
