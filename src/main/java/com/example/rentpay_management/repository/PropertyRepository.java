package com.example.rentpay_management.repository;

import com.example.rentpay_management.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByAreaContainingIgnoreCase(String area);
    List<Property> findByWardContainingIgnoreCase(String ward);
    List<Property> findByShopNameContainingIgnoreCase(String shopName);
    Optional<Property> findByPropertyId(String propertyId);
}