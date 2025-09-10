package com.example.rentpay_management.serviceimpl;

import com.example.rentpay_management.dto.CreateDemandRequestDto;
import com.example.rentpay_management.dto.DemandResponseDto;
import com.example.rentpay_management.dto.VendorResponseDto;
import com.example.rentpay_management.exception.NoVendorFoundException;
import com.example.rentpay_management.exception.RecordNotFoundException;
import com.example.rentpay_management.model.*;
import com.example.rentpay_management.repository.*;
import com.example.rentpay_management.services.NotificationService;
import com.example.rentpay_management.services.RentService;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class RentServiceImpl implements RentService {

    private static Logger log = LoggerFactory.getLogger(RentServiceImpl.class);

    private final UserRepository userRepository;
    private final DemandRepository demandRepository;
    private final PaymentRepository paymentRepository;
    private final PasswordEncoder passwordEncoder;
    private final Map<String, NotificationService> notificationService;
    private final PropertyRepository propertyRepository;
    private final ReconciliationRepository reconciliationRepository;

    public RentServiceImpl(UserRepository userRepository,
                           DemandRepository demandRepository,
                           PaymentRepository paymentRepository,
                           PasswordEncoder passwordEncoder,
                           Map<String, NotificationService> notificationService,
                           PropertyRepository propertyRepository,
                           ReconciliationRepository reconciliationRepository) {
        this.userRepository = userRepository;
        this.demandRepository = demandRepository;
        this.paymentRepository = paymentRepository;
        this.passwordEncoder = passwordEncoder;
        this.notificationService = notificationService;
        this.propertyRepository = propertyRepository;
        this.reconciliationRepository = reconciliationRepository;
    }

    public User createSuperAdminIfMissing() {
        return userRepository.findByEmail("admin@rentpay.com")
                .orElseGet(() -> {
                   User u = User.builder()
                           .name("Super Admin")
                           .email("admin@rentpay.com")
                           .passwordHash(passwordEncoder.encode("Admin123"))
                           .role(User.Role.SUPER_ADMIN)
                           .passwordExpiry(LocalDate.now().plusDays(365))
                           .build();
                   return userRepository.save(u);
                });
    }

    public User createVendor(String name, String email, String rawPassword, String contact, String propertyId){

        Optional<User> checkExistingRecord = userRepository.findByEmail(email);
        if(checkExistingRecord.isPresent()) {
            return updateUser(checkExistingRecord, name, email, rawPassword, contact, propertyId);
        } else {
            return insertUser(name, email, rawPassword, contact, propertyId);
        }
    }

    private User updateUser(Optional<User> checkExistingRecord, String name, String email, String rawPassword, String contact, String propertyId) {
        User user = checkExistingRecord.get();

        if(name != null && !name.isBlank()) user.setName(name);
        if(rawPassword != null && !rawPassword.isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(rawPassword));
            user.setPasswordExpiry(LocalDate.now().plusDays(365));
        }
        if(propertyId != null) {
            Optional<Property> property = propertyRepository.findByPropertyId(propertyId);
            if(property.isPresent()) {
                user.setProperty(property.get());
                user.setPropertyCode(property.get().getPropertyId());
            }
        }
        if(contact != null) {
            user.setContact(contact);
        }

        user = userRepository.save(user);
        return user;
    }

    private User insertUser(String name, String email, String rawPassword, String contact, String propertyId) {
        User user = User.builder()
                .name(name)
                .email(email)
                .contact(contact)
                .passwordHash(passwordEncoder.encode(rawPassword))
                .role(User.Role.VENDOR)
                .passwordExpiry(LocalDate.now().plusDays(365))
                .build();

        Optional<Property> property = propertyRepository.findByPropertyId(propertyId);
        if(property.isPresent()){
            user.setProperty(property.get());
            user.setPropertyCode(property.get().getPropertyId());
        }
        user = userRepository.save(user);
        return user;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public List<DemandResponseDto> getDemandDetailsOfVendor(String email) {
        User u = findByEmail(email);
        List<Demand> demandDetailsList =  demandRepository.findByVendorId(u.getId());
        List<DemandResponseDto> demandResponseDtoList = new ArrayList<>();
        for(Demand demand: demandDetailsList) {
            DemandResponseDto demandResponseDto = DemandResponseDto.builder()
                    .demandId(demand.getId())
                    .amount(demand.getAmount())
                    .status(demand.getStatus().toString())
                    .dueDate(demand.getDueDate())
                    .description(demand.getDescription())
                    .vendorId(u.getId())
                    .vendorName(u.getName())
                    .propertyCode(demand.getProperty().getPropertyId())
                    .build();
            demandResponseDtoList.add(demandResponseDto);
        }

        return demandResponseDtoList;

    }

    public List<VendorResponseDto> listVendors() {
        List<User> vendorsList =  userRepository.findByRole(User.Role.VENDOR);
        List<VendorResponseDto> vendorResponseDtoList = new ArrayList<>();
        if(!ObjectUtils.isEmpty(vendorsList)) {
            for(User vendor: vendorsList) {
                VendorResponseDto vendorResponseDto = new VendorResponseDto();
                vendorResponseDto.setVendorUserId(vendor.getId());
                vendorResponseDto.setName(vendor.getName());
                vendorResponseDto.setEmail(vendor.getEmail());
                vendorResponseDto.setRole(vendor.getRole().toString());
                vendorResponseDto.setShopName(vendor.getProperty().getShopName());
                vendorResponseDto.setArea(vendor.getProperty().getArea());
                vendorResponseDto.setWard(vendor.getProperty().getWard());
                vendorResponseDto.setProperty(vendor.getProperty().getPropertyId());
                vendorResponseDto.setContactNumber(vendor.getContact());
                vendorResponseDtoList.add(vendorResponseDto);
            }
        } else {
            log.info("Vendors not Found");
            throw new NoVendorFoundException("Vendors Records Not Found");
        }

        return vendorResponseDtoList;
    }

    @Transactional
    public DemandResponseDto createDemand(CreateDemandRequestDto createDemandRequestDto) {
        var vendor = userRepository.findById(createDemandRequestDto.getVendorId()).orElseThrow(() -> new RecordNotFoundException("Vendor with ID "
                + createDemandRequestDto.getVendorId() + " not found"));
        var property = propertyRepository.findByPropertyId(createDemandRequestDto.getPropertyId()).orElseThrow(() -> new RecordNotFoundException("Property with ID "
                + createDemandRequestDto.getPropertyId() + " not found")); ;
        Demand demand = Demand.builder()
                .vendor(vendor)
                .property(property)
                .propertyCode(property.getPropertyId())
                .amount(createDemandRequestDto.getAmount())
                .dueDate(createDemandRequestDto.getDueDate())
                .createdAt(LocalDateTime.now())
                .status(Demand.Status.PENDING)
                .description(createDemandRequestDto.getDescription())
                .build();

        Demand saved =  demandRepository.save(demand);

        DemandResponseDto demandResponseDto = DemandResponseDto.builder()
                .demandId(saved.getId())
                .amount(saved.getAmount())
                .status(saved.getStatus().toString())
                .createdAt(saved.getCreatedAt())
//                .createdAt(saved.getCreated())
                .dueDate(saved.getDueDate())
                .description(saved.getDescription())
                .vendorId(saved.getVendor().getId())
                .vendorName(saved.getVendor().getName())
                .propertyCode(saved.getPropertyCode())
                .build();

        return demandResponseDto;
    }

    public Payment recordPayment(Demand demand, Double paymentAmount, LocalDateTime paidAt, String paymentId, String paymentMode, String paymentStatus) {
        Payment p = Payment.builder()
                .demand(demand)
                .paymentAmount(paymentAmount)
                .paidAt(paidAt)
                .paymentId(paymentId)
                .paymentMode(paymentMode)
                .paymentStatus(paymentStatus)
                .build();
        Payment payment = paymentRepository.save(p);
        return payment;
    }

    @Transactional
    public int markOverdue() {
        var overdue = demandRepository.findByDueDateBeforeAndStatus(LocalDate.now(), Demand.Status.PENDING);
        overdue.forEach(d -> d.setStatus(Demand.Status.OVERDUE));
        demandRepository.saveAll(overdue);
        return overdue.size();
    }


    // Bulk vendor onboarding from Excel (sheet columns: name,email,password,shopname,contact,area,ward,property)
    @Transactional
    public List<User> onboardingFromExcel(InputStream is) throws IOException {
        List<User> created = new ArrayList<>();
        try(Workbook wb = new XSSFWorkbook(is)) {
            Sheet sheet = wb.getSheetAt(0);
            for(int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if(row == null) continue;
                String name = getCellString(row.getCell(0));
                String email = getCellString(row.getCell(1));
                String password = getCellString(row.getCell(2));
                String contact = getCellString(row.getCell(3));
                String propertyId = getCellString(row.getCell(4));
                String notificationType = getCellString(row.getCell(5));

                if(email == null || email.isBlank()) continue;
//                if(userRepository.findByEmail(email).isPresent()) continue;

                String finalPassword = (password == null || password.isBlank())
                        ? generateRandomPassword()
                        : password;

                User u = createVendor(name, email, finalPassword, contact, propertyId);
                created.add(u);


                NotificationService service;
                if("EMAIL".equalsIgnoreCase(notificationType)) {
                    service = notificationService.get("emailNotificationService");
                } else {
                    service = notificationService.get("smsNotificationService");
                }

                service.sendNotification(email, "Welcome to RentPay",
                        "Hello " + name + ",\n\n" +
                                "Your account has been created." +
                                "Login using:\n" +
                                "Email: " + email + "\n" +
                                "Password: " + finalPassword + "\n\n" +
                                "Please change your password after login."
                        );
            }
        }
        return created;
    }

    private String getCellString(Cell cell) {
        if(cell == null) return null;
        if(cell.getCellType() == CellType.STRING) return cell.getStringCellValue();
        if(cell.getCellType() == CellType.NUMERIC) return String.valueOf((long)cell.getNumericCellValue());
        return null;
    }

    private String generateRandomPassword() {
        return "Pwd@" + UUID.randomUUID().toString().substring(0,6);
    }

    public Map<String, Map<String, Object>> areaWiseReport() {
        Map<String, Map<String, Object>> out = new HashMap<>();
        List<User> vendors = userRepository.findAll();
        for(User v: vendors) {
            if(!v.getRole().toString().equalsIgnoreCase("VENDOR")) {
                continue;
            }
            Property propertyDetails = v.getProperty();
            String area = (propertyDetails == null || propertyDetails.getArea() == null)? "UNKNOWN": propertyDetails.getArea();
            var map = out.computeIfAbsent(area, k -> {
                Map<String, Object> m = new HashMap<>();
                m.put("demandCount", 0);
                m.put("demandTotal", 0.0);
                m.put("demandPaid", 0.0);
                return m;
            });

            var demands = demandRepository.findByVendorId(v.getId());
            if(demands != null) {
                int demandCount = demands.size();
                double demandTotal = demands.stream().map(d -> d.getAmount().doubleValue()).mapToDouble(Double::doubleValue).sum();
                double demandPaid = demands.stream().filter(d -> d.getStatus() == Demand.Status.PAID).mapToDouble(d -> d.getAmount().doubleValue()).sum();
                map.put("demandCount", ((Integer)map.get("demandCount")) + demandCount);
                map.put("demandTotal", ((Double)map.get("demandTotal")) + demandTotal);
                map.put("demandPaid",  ((Double)map.get("demandPaid")) + demandPaid);
            }
        }

        return out;
    }

    public Map<String, Map<String, Object>> wardWiseReport() {
        Map<String, Map<String, Object>> out = new HashMap<>();
        List<User> vendors = userRepository.findAll();
        for(User v: vendors) {
            if(!v.getRole().toString().equalsIgnoreCase("VENDOR")) {
                continue;
            }
            Property propertyDetails = v.getProperty();
            String ward = (propertyDetails == null || propertyDetails.getWard() == null)? "UNKNOWN": propertyDetails.getWard();
            var map = out.computeIfAbsent(ward, k -> {
                Map<String, Object> m = new HashMap<>();
                m.put("demandCount", 0);
                m.put("demandTotal", 0.0);
                m.put("demandPaid", 0.0);
                return m;
            });

            var demands = demandRepository.findByVendorId(v.getId());
            if(demands != null) {
                int demandCount = demands.size();
                double demandTotal = demands.stream().map(d -> d.getAmount().doubleValue()).mapToDouble(Double::doubleValue).sum();
                double demandPaid = demands.stream().filter(d -> d.getStatus() == Demand.Status.PAID).mapToDouble(d -> d.getAmount().doubleValue()).sum();
                map.put("demandCount", ((Integer)map.get("demandCount")) + demandCount);
                map.put("demandTotal", ((Double)map.get("demandTotal")) + demandTotal);
                map.put("demandPaid",  ((Double)map.get("demandPaid")) + demandPaid);
            }
        }

        return out;
    }

    public Map<String, Map<String, Object>> propertyWiseReport() {
        Map<String, Map<String, Object>> out = new HashMap<>();
        List<User> vendors = userRepository.findAll();
        for(User v: vendors) {
            if(!v.getRole().toString().equalsIgnoreCase("VENDOR")) {
                continue;
            }
            Property propertyDetails = v.getProperty();
            String propertyId = (propertyDetails == null || propertyDetails.getPropertyId() == null)? "UNKNOWN": propertyDetails.getPropertyId();
            var map = out.computeIfAbsent(propertyId, k -> {
                Map<String, Object> m = new HashMap<>();
                m.put("demandCount", 0);
                m.put("demandTotal", 0.0);
                m.put("demandPaid", 0.0);
                return m;
            });

            var demands = demandRepository.findByVendorId(v.getId());
            if(demands != null) {
                int demandCount = demands.size();
                double demandTotal = demands.stream().map(d -> d.getAmount().doubleValue()).mapToDouble(Double::doubleValue).sum();
                double demandPaid = demands.stream().filter(d -> d.getStatus() == Demand.Status.PAID).mapToDouble(d -> d.getAmount().doubleValue()).sum();
                map.put("demandCount", ((Integer)map.get("demandCount")) + demandCount);
                map.put("demandTotal", ((Double)map.get("demandTotal")) + demandTotal);
                map.put("demandPaid",  ((Double)map.get("demandPaid")) + demandPaid);
            }
        }

        return out;
    }

    public Reconciliation createRecon(Demand demand, Double paymentAmount, LocalDateTime paidAt, String paymentId, String paymentMode, String paymentStatus) {

        Reconciliation reconciliation =  Reconciliation.builder()
                .vendorName(demand.getVendor().getName())
                .vendorId(demand.getVendor().getId())
                .demandId(demand.getId())
                .propertyCode(demand.getPropertyCode())
                .paymentAmount(paymentAmount)
                .paidAt(paidAt)
                .paymentId(paymentId)
                .paymentMode(paymentMode)
                .paymentStatus(paymentStatus)
                .build();

        double amount = paymentAmount;
        double serviceChargePercentage = 1.9;
        double serviceTaxPercentage = 0.342;

        Double orderAmount = paymentAmount;
        Double serviceCharge = (amount * serviceChargePercentage) / 100;
        Double serviceTax = (amount * serviceTaxPercentage) / 100;
        Double settlementAmount = orderAmount - serviceCharge - serviceTax;

        reconciliation.setOrderAmount(orderAmount);
        reconciliation.setServiceCharge(serviceCharge);
        reconciliation.setServiceTax(serviceTax);
        reconciliation.setSettlementAmount(settlementAmount);

        return reconciliationRepository.save(reconciliation);
    }

    public List<Reconciliation> getReconciliationData() {
        List<Reconciliation> reconciliations = reconciliationRepository.findAll();
        return reconciliations;
    }

    public VendorResponseDto getVendorDetails(String email) {
        User vendor = findByEmail(email);
        VendorResponseDto vendorResponseDto = new VendorResponseDto();
        vendorResponseDto.setVendorUserId(vendor.getId());
        vendorResponseDto.setName(vendor.getName());
        vendorResponseDto.setEmail(vendor.getEmail());
        vendorResponseDto.setRole(vendor.getRole().toString());
        vendorResponseDto.setShopName(vendor.getProperty().getShopName());
        vendorResponseDto.setArea(vendor.getProperty().getArea());
        vendorResponseDto.setWard(vendor.getProperty().getWard());
        vendorResponseDto.setProperty(vendor.getProperty().getPropertyId());
        vendorResponseDto.setContactNumber(vendor.getContact());
        return vendorResponseDto;
    }


}
