//package vnpt_it.vn.accountservice.notification;
//
//import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
//import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//@Component
//public class NotificationServiceFallback implements NotificationService {
//    Logger logger = LoggerFactory.getLogger(NotificationServiceFallback.class);
//
//    @CircuitBreaker(name = "notification-service", fallbackMethod = "fallbackCircuitBreakerOPEN")
//    @Override
//    public void sendNotification(MessageDTO messageDTO) {
//        logger.error("Error connect or exception from Notification Service");
//        throw new RuntimeException("Error connect or exception from Notification Service");
//    }
//
//    public void fallbackCircuitBreakerOPEN(CallNotPermittedException ex) {
//        // Xử lý khi CircuitBreaker OPEN
//        logger.error("CallNotPermittedException");
//        throw ex;
//    }
//}
