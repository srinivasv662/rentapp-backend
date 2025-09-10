package com.example.rentpay_management.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDto {

    @NotNull(message = "Email ID is required")
    private String email;

    @NotNull(message = "Password is required")
    private String password;

//    public AuthRequest() {}
//
//    public AuthRequest(String email, String password) {
//        this.email = email;
//        this.password = password;
//    }

//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
}
