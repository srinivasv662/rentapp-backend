package com.example.rentpay_management.serviceimpl;

import com.example.rentpay_management.exception.SignatureIncorrectException;
import com.example.rentpay_management.model.Demand;
import com.example.rentpay_management.model.Payment;
import com.example.rentpay_management.model.Reconciliation;
import com.example.rentpay_management.repository.DemandRepository;
import com.example.rentpay_management.repository.PaymentRepository;
import com.example.rentpay_management.services.RentService;
import com.example.rentpay_management.services.WebhookService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WebhookServiceImpl implements WebhookService {

    private static final Logger log = LoggerFactory.getLogger(WebhookServiceImpl.class);

    @Value("${cashfree.clientSecret}")
    private String clientSecret;

    private final DemandRepository demandRepository;
    private final PaymentRepository paymentRepository;
    private final RentService rentServiceImpl;

    public String webhookHandler(String payload, String signature, String timeStamp) throws Exception {
        String data = timeStamp + payload;
        String secretKey = clientSecret;
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key_spec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key_spec);
        String computed_signature = Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(data.getBytes()));
        if (computed_signature.equals(signature)) {
            Gson g = new Gson();
            Object response = g.fromJson(payload, Object.class);
            log.info("Original Signature that was passed in the request{}", signature);
            log.info("Computed Signature {}", computed_signature);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(payload);

            if (Objects.equals(jsonNode.get("type").asText(), "WEBHOOK")) {
                return "WEBHOOK_TEST";
            }

//            String orderId = jsonNode.get("order_id").asText();
//            String status = jsonNode.get("payment_status").asText(); //NullPointerException
            String orderId = jsonNode.get("data").get("order").get("order_id").asText();
            String status = jsonNode.get("data").get("payment").get("payment_status").asText();
            Demand demand = demandRepository.findByCashfreeOrderId(orderId);
            if (demand != null && status.equalsIgnoreCase("SUCCESS")) {
                demand.setStatus(Demand.Status.PAID);
                demandRepository.save(demand);
            }
//            else if(demand!= null) {
//                // keep the previous status as it is
//                demand.setStatus(Demand.Status.PENDING);
//                demandRepository.save(demand);
//            }

            // Not yet tested
//            Payment payment = new Payment();
//            payment.setDemand(demand);
//            payment.setPaidAmount(Double.parseDouble(jsonNode.get("data").get("order").get("order_amount").asText()));
////            payment.setPaidAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
//            payment.setPaidAt(LocalDateTime.now());
//            payment.setPaymentId(jsonNode.get("data").get("payment").get("cf_payment_id").asText());
//            payment.setPaymentMode(jsonNode.get("data").get("payment").get("payment_group").asText());
//            paymentRepository.save(payment);

            Double paymentAmount = Double.parseDouble(jsonNode.get("data").get("order").get("order_amount").asText());
            LocalDateTime paidAt = LocalDateTime.now();
            String paymentId = jsonNode.get("data").get("payment").get("cf_payment_id").asText();
            String paymentMode = jsonNode.get("data").get("payment").get("payment_group").asText();
            Payment payment = rentServiceImpl.recordPayment(demand, paymentAmount, paidAt, paymentId, paymentMode, status);

            Reconciliation reconciliation = rentServiceImpl.createRecon(demand, paymentAmount, paidAt, paymentId, paymentMode, status);
            if(reconciliation != null) {
                log.info("Reconciliation Record saved succcessfully");
            }

            if (payment != null) {
                if(status.equalsIgnoreCase("SUCCESS")){
                    log.info("Payment Success");
                    return "SUCCESS";
                }
                else{
                    log.info("Payment Failed");
                    return "FAILED";
                }
            }
        }
        throw new SignatureIncorrectException("Generated signature and received signature did not match.");
    }

}
