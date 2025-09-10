package com.example.rentpay_management.serviceimpl;

import com.example.rentpay_management.dto.DemandResForProperty;
import com.example.rentpay_management.dto.PropertyResponseDto;
import com.example.rentpay_management.dto.UserRepsonseDto;
import com.example.rentpay_management.model.Demand;
import com.example.rentpay_management.model.Property;
import com.example.rentpay_management.model.User;
import com.example.rentpay_management.repository.PropertyRepository;
import com.example.rentpay_management.services.PropertyOnboardingService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PropertyOnboardingServiceImpl implements PropertyOnboardingService {

    private final PropertyRepository propertyRepository;

    public List<Property> uploadProperties(MultipartFile file) {
        List<Property> properties = new ArrayList<>();

        System.out.println("T1");
        try(Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            boolean firstRow = true;
            while(rows.hasNext()) {
                Row row = rows.next();

                // skip header row
                if(firstRow) {
                    firstRow = false;
                    continue;
                }

                System.out.println("T2");

                String propertyId = getCellValue(row.getCell(0));
                String shopName = getCellValue(row.getCell(1));
                String area = getCellValue(row.getCell(2));
                String ward = getCellValue(row.getCell(3));

                System.out.println("T3 " + propertyId);
                Optional<Property> checkExistingRecords = propertyRepository.findByPropertyId(propertyId);
                Property property;
                if(checkExistingRecords.isPresent()) {
                    System.out.println("T4");
                    property = updateProperty(checkExistingRecords, propertyId, shopName, area, ward, false);
                } else {
                    System.out.println("T5");
                    property = createProperty(propertyId, shopName, area, ward, false);
                }


                properties.add(property);
            }

            return propertyRepository.saveAll(properties);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getCellValue(Cell cell) {
        if(cell == null) return null;
        if(cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((long) cell.getNumericCellValue());
        }
        return cell.getStringCellValue();
    }

    public Property createProperty(String propertyId, String shopName, String area, String ward, boolean manual) {

        Property property = Property.builder()
                .propertyId(propertyId)
                .shopName(shopName)
                .area(area)
                .ward(ward)
                .build();

        if(manual) propertyRepository.save(property);

        return property;
    }

    public Property updateProperty(Optional<Property> checkExistingRecords, String propertyId, String shopName, String area, String ward, boolean manual) {
        Property property = checkExistingRecords.get();

        if(shopName != null) property.setShopName(shopName);
        if(area != null) property.setArea(area);
        if(ward != null) property.setWard(ward);

        if(manual) propertyRepository.save(property);

        return property;
    }

//    public List<PropertyResponseDto> getPropertyDetails() {
//        List<Property> properties =propertyRepository.findAll();
//        List<PropertyResponseDto> propertyResponseDtos = new ArrayList<>();
//        for(Property property: properties) {
//            PropertyResponseDto propertyResponseDto = new PropertyResponseDto();
//            propertyResponseDto.setPropertyId(property.getPropertyId());
//            propertyResponseDto.setArea(property.getArea());
//            propertyResponseDto.setWard(property.getWard());
//            propertyResponseDto.setShopName(property.getShopName());
//            List<Demand> demands = property.getDemands();
//            List<DemandResForProperty> demandResForPropertyList = new ArrayList<>();
//            for(Demand demand: demands) {
//                DemandResForProperty demandResForProperty = new DemandResForProperty();
//                demandResForProperty.setDemandId(demand.getId());
//                demandResForProperty.setAmount(demand.getAmount());
//                demandResForProperty.setStatus(demand.getStatus());
//                demandResForProperty.setDescription(demand.getDescription());
//                demandResForProperty.setDueDate(demand.getDueDate());
//                demandResForProperty.setCreatedAt(demand.getCreatedAt());
//                UserRepsonseDto userRepsonseDto = new UserRepsonseDto();
//                User user = demand.getVendor();
//                userRepsonseDto.setUserId(user.getId());
//                userRepsonseDto.setEmail(user.getEmail());
//                userRepsonseDto.setName(user.getName());
//                userRepsonseDto.setRole(user.getRole());
//                userRepsonseDto.setContact(user.getContact());
//                demandResForProperty.setUserRepsonseDto(userRepsonseDto);
//                demandResForPropertyList.add(demandResForProperty);
//            }
//            propertyResponseDto.setDemandResForPropertyList(demandResForPropertyList);
//            propertyResponseDtos.add(propertyResponseDto);
//        }
//
//        return propertyResponseDtos;
//    }

    public List<PropertyResponseDto> getPropertyDetails() {
        List<Property> properties =propertyRepository.findAll();
        List<PropertyResponseDto> propertyResponseDtos = new ArrayList<>();
        for(Property property: properties) {
            PropertyResponseDto propertyResponseDto = new PropertyResponseDto();
            propertyResponseDto.setPropertyId(property.getPropertyId());
            propertyResponseDto.setArea(property.getArea());
            propertyResponseDto.setWard(property.getWard());
            propertyResponseDto.setShopName(property.getShopName());
            List<User> users = property.getVendors();
            List<UserRepsonseDto> userRepsonseDtoList = new ArrayList<>();
            for(User user: users) {
                UserRepsonseDto userRepsonseDto = new UserRepsonseDto();
                userRepsonseDto.setUserId(user.getId());
                userRepsonseDto.setName(user.getName());
                userRepsonseDto.setEmail(user.getEmail());
                userRepsonseDto.setRole(user.getRole());
                userRepsonseDto.setContact(user.getContact());
                List<Demand> demands = user.getDemands();
                List<DemandResForProperty> demandResForPropertyList = new ArrayList<>();
                for(Demand demand: demands) {
                    DemandResForProperty demandResForProperty = new DemandResForProperty();
                    demandResForProperty.setCreatedAt(demand.getCreatedAt());
//                    demandResForProperty.setCreatedAt(demand.getCreated());
                    demandResForProperty.setDemandId(demand.getId());
                    demandResForProperty.setDueDate(demand.getDueDate());
                    demandResForProperty.setStatus(demand.getStatus());
                    demandResForProperty.setAmount(demand.getAmount());
                    demandResForProperty.setDescription(demand.getDescription());
                    demandResForPropertyList.add(demandResForProperty);
                }
                userRepsonseDto.setDemandResForPropertyList(demandResForPropertyList);
                userRepsonseDtoList.add(userRepsonseDto);
            }
            propertyResponseDto.setUserRepsonseDtos(userRepsonseDtoList);
            propertyResponseDtos.add(propertyResponseDto);
        }

        return propertyResponseDtos;
    }

}
