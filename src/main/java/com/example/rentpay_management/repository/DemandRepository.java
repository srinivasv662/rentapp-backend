package com.example.rentpay_management.repository;

import com.example.rentpay_management.model.Demand;
import com.example.rentpay_management.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DemandRepository extends JpaRepository<Demand, Long> {

    List<Demand> findByVendor(User vendor);
    List<Demand> findByStatus(Demand.Status status);
    List<Demand> findByVendorId(Long vendorId);
    List<Demand> findByDueDateBeforeAndStatus(LocalDate date, Demand.Status status);
    Demand findByVendorIdAndId(Long vendorId, Long demandId);

    Demand findByCashfreeOrderId(String orderId);

}
