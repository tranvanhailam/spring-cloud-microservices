package vnpt_it.vn.notificationservice.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vnpt_it.vn.notificationservice.model.MessageDTO;
import vnpt_it.vn.notificationservice.service.NotificationService;

@RestController
public class NotificationController {
    private final NotificationService notificationService;
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send-notification")
    public void sendNotification(@RequestBody MessageDTO messageDTO) {
        this.notificationService.sendEmail(messageDTO);
    }
}
