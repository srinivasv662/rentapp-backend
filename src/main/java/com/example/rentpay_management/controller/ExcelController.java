package com.example.rentpay_management.controller;

import com.example.rentpay_management.model.Property;
import com.example.rentpay_management.serviceimpl.RentServiceImpl;
import com.example.rentpay_management.services.PropertyOnboardingService;
import com.example.rentpay_management.services.RentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class ExcelController {

    private final RentService rentServiceImpl;
    private final PropertyOnboardingService propertyOnboardingService;

    public ExcelController(RentServiceImpl rentServiceImpl, PropertyOnboardingService propertyOnboardingService) {
        this.rentServiceImpl = rentServiceImpl;
        this.propertyOnboardingService = propertyOnboardingService;
    }

    @PostMapping("/vendors/upload")
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file) throws Exception {
        try {
            var users = rentServiceImpl.onboardingFromExcel(file.getInputStream());
            return ResponseEntity.ok(Map.of("created", users.size()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Something went wrong", "details", e.getMessage()));
        }
    }

    @PostMapping("/properties/upload")
    public ResponseEntity<?> uploadProperties(@RequestParam("file") MultipartFile file) {
        try {
            List<Property> properties = propertyOnboardingService.uploadProperties(file);
            return ResponseEntity.ok().body(Map.of("created", properties.size()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
