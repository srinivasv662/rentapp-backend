package com.example.rentpay_management.repository;

import com.example.rentpay_management.model.Demand;
import com.example.rentpay_management.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByDemand(Demand demand);
    List<Payment> findByDemandId(Long demandId);

}
