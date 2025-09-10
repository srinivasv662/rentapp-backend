package com.example.rentpay_management.serviceimpl;

import com.example.rentpay_management.services.DemandSchedulerService;
import com.example.rentpay_management.services.RentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DemandSchedulerServiceImpl implements DemandSchedulerService {

    private static final Logger log = LoggerFactory.getLogger(DemandSchedulerServiceImpl.class);

    private final RentService rentServiceImpl;

    @Scheduled(cron = "0 0 8 * * *", zone = "Asia/Kolkata")
    public void runOverdurJob() {
        log.info("Overdue Job execution started");
        int updated = rentServiceImpl.markOverdue();
        log.info("Overdue job executed. Demands updated = " + updated);
    }

}
