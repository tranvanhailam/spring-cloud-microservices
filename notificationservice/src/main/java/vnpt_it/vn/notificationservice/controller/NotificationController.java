package vnpt_it.vn.notificationservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vnpt_it.vn.notificationservice.domain.MessageDTO;
import vnpt_it.vn.notificationservice.service.NotificationService;

@RestController
public class NotificationController {
    private final NotificationService notificationService;
    private final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send-notification")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER','SCOPE_notification')")
    public void sendNotification(@RequestBody MessageDTO messageDTO) {
        logger.info(">>>>>>>>>> NotificationService NotificationController: sendNotification");
        this.notificationService.sendEmail(messageDTO);
    }
}
