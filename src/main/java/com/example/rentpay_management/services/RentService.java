package com.example.rentpay_management.services;

import com.example.rentpay_management.dto.CreateDemandRequestDto;
import com.example.rentpay_management.dto.DemandResponseDto;
import com.example.rentpay_management.dto.VendorResponseDto;
import com.example.rentpay_management.model.Demand;
import com.example.rentpay_management.model.Payment;
import com.example.rentpay_management.model.Reconciliation;
import com.example.rentpay_management.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface RentService {

    public User createVendor(String name, String email, String rawPassword, String contact,String propertyId);

    public User findByEmail(String email);

    public List<VendorResponseDto> listVendors();

    public DemandResponseDto createDemand(CreateDemandRequestDto createDemandRequestDto);

    public Payment recordPayment(Demand demand, Double paidAmount, LocalDateTime paidAt, String paymentId, String paymentMode,String paymentStatus);

    public int markOverdue();

    public User createSuperAdminIfMissing();

    public List<User> onboardingFromExcel(InputStream is) throws IOException;

    public Map<String, Map<String, Object>> areaWiseReport();

    public Map<String, Map<String, Object>> propertyWiseReport();

    public Map<String, Map<String, Object>> wardWiseReport();

    public List<DemandResponseDto> getDemandDetailsOfVendor(String email);

    public Reconciliation createRecon(Demand demand, Double paidAmount, LocalDateTime paidAt, String paymentId, String paymentMode, String paymentStatus);

    public List<Reconciliation> getReconciliationData();

    public VendorResponseDto getVendorDetails(String email);

}
