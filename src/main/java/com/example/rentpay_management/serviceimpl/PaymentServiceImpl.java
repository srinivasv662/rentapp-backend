package com.example.rentpay_management.serviceimpl;

import com.cashfree.pg.ApiException;
import com.cashfree.pg.ApiResponse;
import com.cashfree.pg.Cashfree;
import com.cashfree.pg.model.CreateOrderRequest;
import com.cashfree.pg.model.CustomerDetails;
import com.cashfree.pg.model.OrderEntity;
import com.cashfree.pg.model.OrderMeta;
import com.example.rentpay_management.exception.VendorNotFoundException;
import com.example.rentpay_management.model.Demand;
import com.example.rentpay_management.model.User;
import com.example.rentpay_management.repository.DemandRepository;
import com.example.rentpay_management.repository.PaymentRepository;
import com.example.rentpay_management.repository.UserRepository;
import com.example.rentpay_management.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final RestTemplate restTemplate = new RestTemplate();
    private final DemandRepository demandRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;

    @Value("${cashfree.clientId}")
    private String clientId;

    @Value("${cashfree.clientSecret}")
    private String clientSecret;

    @Value("${cashfree.cashfreeBaseUrl}")
    private String cashfreeBaseUrl;

    @Value("${NOTIFY_URL}")
    private String notifyUrl;

    @Value("${return_url}")
    private String returnUrl;


    public ApiResponse<OrderEntity> createOrder(Long vendorId, Long demandId, Double amount) {
        //1. Prepare Cashfree Order request
        CustomerDetails customerDetails = new CustomerDetails();
        Optional<User> vendor = userRepository.findById(vendorId);
        if(!vendor.isPresent() || vendor.isEmpty() ||  vendor == null) {
            throw new VendorNotFoundException("VendorProfile not found for userId " + vendorId);
        }
        User user = vendor.get();
        customerDetails.setCustomerId(user.getId().toString());
        customerDetails.setCustomerPhone(user.getContact());
        customerDetails.setCustomerName(user.getName());
        customerDetails.setCustomerEmail(user.getEmail());

        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setOrderAmount(BigDecimal.valueOf(amount));
        createOrderRequest.setOrderCurrency("INR");
        createOrderRequest.setCustomerDetails(customerDetails);
        createOrderRequest.setOrderId("order_" + System.currentTimeMillis());

        OrderMeta orderMeta = new OrderMeta();
        orderMeta.setNotifyUrl(notifyUrl);
        orderMeta.setReturnUrl(returnUrl);
        createOrderRequest.setOrderMeta(orderMeta);
        try {

            Demand demand = demandRepository.findByVendorIdAndId(vendorId, demandId);
            if(demand!= null && demand.getStatus().toString().equalsIgnoreCase("PAID")) {
                throw new IllegalStateException("Payment already done for this demand");
            }

            Cashfree cashfree = new Cashfree(Cashfree.SANDBOX, clientId, clientSecret, null, null, null);
            ApiResponse<OrderEntity> response = cashfree.PGCreateOrder(createOrderRequest,null, null, null);
            if(response != null && demand != null) {
                log.info("Order Id is: {}", response.getData().getOrderId());
                demand.setCashfreeOrderId(response.getData().getOrderId());
                demandRepository.save(demand);
            }

            return response;
        } catch (ApiException e) {
            throw new RuntimeException("Cashfree API error: " + e.getMessage(), e);
        }
    }

}
