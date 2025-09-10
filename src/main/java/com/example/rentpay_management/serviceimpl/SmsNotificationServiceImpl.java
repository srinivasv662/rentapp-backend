package com.example.rentpay_management.serviceimpl;

import com.example.rentpay_management.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service("smsNotificationService")
@RequiredArgsConstructor
public class SmsNotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(SmsNotificationServiceImpl.class);

    public void sendNotification(String to, String subject, String text) {
        log.info("SMS implementation");
    }

}
