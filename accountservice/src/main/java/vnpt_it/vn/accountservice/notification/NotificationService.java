//package vnpt_it.vn.accountservice.notification;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//@FeignClient(name = "notification-service", fallback = NotificationServiceFallback.class)
//public interface NotificationService {
//    @PostMapping("/send-notification")
//    public void sendNotification(@RequestBody MessageDTO messageDTO);
//}
