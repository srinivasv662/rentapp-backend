package com.example.rentpay_management.dto;

import com.example.rentpay_management.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRepsonseDto {

    private Long userId;

    private String name;

    private String email;

    private String contact;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private User.Role role;

    List<DemandResForProperty> demandResForPropertyList;

}
