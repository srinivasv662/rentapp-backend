package com.example.rentpay_management.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
    private String token;
    private String role;
    private Long userId;

//    public AuthResponse(String token, String role, Long userId) {
//        this.token = token;
//        this.role = role;
//        this.userId = userId;
//    }

//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }
//
//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }
//
//    public Long getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Long userId) {
//        this.userId = userId;
//    }
}
