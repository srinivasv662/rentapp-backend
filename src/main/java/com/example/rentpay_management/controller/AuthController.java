package com.example.rentpay_management.controller;

import com.example.rentpay_management.config.JwtUtil;
import com.example.rentpay_management.dto.AuthRequestDto;
import com.example.rentpay_management.dto.AuthResponseDto;
import com.example.rentpay_management.dto.ErrorResponseDto;
import com.example.rentpay_management.model.User;
import com.example.rentpay_management.serviceimpl.RentServiceImpl;
import com.example.rentpay_management.services.RentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final RentService rentServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(RentServiceImpl rentServiceImpl, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.rentServiceImpl = rentServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login1")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequestDto req) {
        log.info("Login attempt for email [{}]", req.getEmail());

        User u = rentServiceImpl.findByEmail(req.getEmail());
        if(u == null || !passwordEncoder.matches(req.getPassword(), u.getPasswordHash())) {
            log.warn("Invalid credentials for email [{}]", req.getEmail());
            return ResponseEntity.status(401).body(new ErrorResponseDto("INVALID CREDENTIALS", "Email or password is incorrect"));
        }
//        log.info("Email Present");

//        if(!passwordEncoder.matches(req.getPassword(), u.getPasswordHash())) {
//            return ResponseEntity.status(401).body("Invalid");
//        }

        log.info("Password matched with the email/user");
        if(u.getPasswordExpiry().isBefore(LocalDate.now())) {
            log.warn("Password expired for user [{}]", u.getId());
            return ResponseEntity.status(401).body(new ErrorResponseDto("PASSWORD_EXPIRED", "Password expired, please reset"));
        }

        String token = jwtUtil.generateToken(u.getEmail(), u.getRole().name());
        log.info("Token Generated");
        log.info("User [{}] logged in successfully", u.getId());

        return ResponseEntity.ok(new AuthResponseDto(token, u.getRole().name(), u.getId()));
    }



}
