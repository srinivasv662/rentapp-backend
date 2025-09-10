package com.example.rentpay_management.controller;

import com.example.rentpay_management.dto.*;
import com.example.rentpay_management.model.Property;
import com.example.rentpay_management.model.Reconciliation;
import com.example.rentpay_management.repository.PropertyRepository;
import com.example.rentpay_management.repository.UserRepository;
import com.example.rentpay_management.serviceimpl.PropertyOnboardingServiceImpl;
import com.example.rentpay_management.serviceimpl.RentServiceImpl;
import com.example.rentpay_management.services.PropertyOnboardingService;
import com.example.rentpay_management.services.RentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    private final RentService rentServiceImpl;
    private final PropertyOnboardingService propertyOnboardingService;
    private final UserRepository userRepository;

    public AdminController(RentServiceImpl rentServiceImpl, PropertyOnboardingService propertyOnboardingService, UserRepository userRepository) {
        this.rentServiceImpl = rentServiceImpl;
        this.propertyOnboardingService =propertyOnboardingService;
        this.userRepository = userRepository;
    }


    @PostMapping("/demands")
    public ResponseEntity<DemandResponseDto> createDemand1(@Valid @RequestBody CreateDemandRequestDto req) {

        log.info("Creating demand for vendorId={}, propertyId={}", req.getVendorId(), req.getPropertyId());

        DemandResponseDto response = rentServiceImpl.createDemand(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/vendors")
    public ResponseEntity<List<VendorResponseDto>> listVendors() {
        List<VendorResponseDto> vendors = rentServiceImpl.listVendors();
        return ResponseEntity.ok(vendors);
    }

    @GetMapping("/reports/area")
    public ResponseEntity<?> areaReport() {
        try {
            log.info("Area Report Started");
            Map<String, Map<String, Object>> report =  rentServiceImpl.areaWiseReport();
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            log.error("Something went wrong, details: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "Something went wrong", "details", e.getMessage()));
        }

    }

    @GetMapping("/reports/ward")
    public ResponseEntity<?> wardReport() {
        try {
            log.info("Ward Report Started");
            Map<String, Map<String, Object>> report =  rentServiceImpl.wardWiseReport();
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            log.error("Something went wrong, details: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "Something went wrong", "details", e.getMessage()));
        }

    }

    @GetMapping("/reports/property")
    public ResponseEntity<?> propertyReport() {
        try {
            log.info("Property Report Started");
            Map<String, Map<String, Object>> report =  rentServiceImpl.propertyWiseReport();
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            log.error("Something went wrong, details: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", "Something went wrong", "details", e.getMessage()));
        }


    }

    @PostMapping("/demands/mark-overdue")
    public ResponseEntity<Map<String, Object>> markOverdue(){
        log.info("Mark Overdue Started");
        int count = rentServiceImpl.markOverdue();
        return ResponseEntity.ok(Map.of(
                "message", "Overdue demands marked successfully",
                "count", count
        ));
    }

    @GetMapping("/properties")
    public ResponseEntity<List<PropertyResponseDto>> getProperties() {
        log.info("Get Properties Started");
        List<PropertyResponseDto> result = propertyOnboardingService.getPropertyDetails();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/reconciliation")
    public ResponseEntity<List<Reconciliation>> getReconciliationrecords() {
        log.info("Get ReconciliationRecords Started");
        List<Reconciliation> result = rentServiceImpl.getReconciliationData();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/vendors/{vendorId}/properties")
    public ResponseEntity<List<String>> getVendorProperties(@PathVariable Long vendorId) {
        List<String> propertyIds = userRepository.findPropertyIdsByVendorId(vendorId);
        return ResponseEntity.ok(propertyIds);
    }
}
