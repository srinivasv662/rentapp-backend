package com.example.rentpay_management.services;

import com.example.rentpay_management.dto.PropertyResponseDto;
import com.example.rentpay_management.model.Property;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PropertyOnboardingService {

    public List<Property> uploadProperties(MultipartFile file);

    public Property createProperty(String propertyId, String shopName, String area, String ward, boolean manual);

    public List<PropertyResponseDto> getPropertyDetails();
}
