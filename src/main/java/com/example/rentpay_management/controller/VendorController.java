package com.example.rentpay_management.controller;

import com.example.rentpay_management.dto.DemandResponseDto;
import com.example.rentpay_management.dto.VendorResponseDto;
import com.example.rentpay_management.repository.DemandRepository;
import com.example.rentpay_management.serviceimpl.RentServiceImpl;
import com.example.rentpay_management.services.RentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vendor")
//@PreAuthorize("hasRole('VENDOR')")
//@PreAuthorize("hasAuthority('VENDOR')")
public class VendorController {

    private static final Logger log = LoggerFactory.getLogger(VendorController.class);

    private final RentService rentServiceImpl;
    private final DemandRepository demandRepository;

    public VendorController(RentServiceImpl rentServiceImpl, DemandRepository demandRepository) {
        this.rentServiceImpl = rentServiceImpl;
        this.demandRepository = demandRepository;
    }

    @GetMapping("/demands")
    public ResponseEntity<List<DemandResponseDto>> myDemands(Authentication auth) {
        log.info(("/demand api got hit"));
        String email = auth.getName();
        log.info("User: {}", email);

        return ResponseEntity.ok().body(rentServiceImpl.getDemandDetailsOfVendor(email));
    }

    @GetMapping("/details")
    public ResponseEntity<VendorResponseDto> getVendorDetails(Authentication auth) {
        log.info(("getVendorDetails Started"));
        String email = auth.getName();
        log.info("User: {}", email);

        return ResponseEntity.ok().body(rentServiceImpl.getVendorDetails(email));
    }

}
