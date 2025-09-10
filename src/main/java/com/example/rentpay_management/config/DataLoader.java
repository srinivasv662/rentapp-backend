//package com.example.rentpay_management.config;
//
//import com.example.rentpay_management.serviceimpl.RentServiceImpl;
//import com.example.rentpay_management.services.PropertyOnboardingService;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class DataLoader {
//
//    @Bean
//    CommandLineRunner run(RentServiceImpl rentServiceImpl, PropertyOnboardingService propertyOnboardingService) {
//        return args -> {
//            rentServiceImpl.createSuperAdminIfMissing();
//            propertyOnboardingService.createProperty("PH-110", "shop10", "area10", "ward10", true);
//            rentServiceImpl.createVendor("Acme Rentals", "vendor1@example.com", "Vendor@123", "9999999999","PH-110");
//
//        };
//    }
//
//}
