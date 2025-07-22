package vnpt_it.vn.accountservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vnpt_it.vn.accountservice.model.MessageDTO;

@FeignClient(name = "notification-service", fallback = NotificationServiceFallback.class)
public interface NotificationService {
    @PostMapping("/send-notification")
    public void sendNotification(@RequestBody MessageDTO messageDTO);
}
