package vnpt_it.vn.subscriberservice.controller;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vnpt_it.vn.subscriberservice.service.EmailService;

@Component
@EnableScheduling
public class EmailScheduler {
    private final EmailService emailService;

    public EmailScheduler(EmailService emailService) {
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 8 ? * SAT") // Gửi email vào mỗi sáng thứ 7 hàng tuần
    @Transactional
    public void scheduledEmailJob() {
        this.emailService.handleSendEmail();
    }
}
