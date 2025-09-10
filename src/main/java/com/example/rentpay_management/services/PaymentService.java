package com.example.rentpay_management.services;

import com.cashfree.pg.ApiResponse;
import com.cashfree.pg.model.OrderEntity;

public interface PaymentService {

    public ApiResponse<OrderEntity> createOrder(Long vendorId, Long demandId, Double amount);

}
