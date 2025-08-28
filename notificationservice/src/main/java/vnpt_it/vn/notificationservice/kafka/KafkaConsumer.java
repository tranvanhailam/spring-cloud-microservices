package vnpt_it.vn.notificationservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import vnpt_it.vn.notificationservice.domain.MessageJobIntroductionDTO;
import vnpt_it.vn.notificationservice.service.NotificationService;

@Service
public class KafkaConsumer {

    //    @Value(value = "${spring.kafka.consumer.group-id}")
//    private String groupId;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public KafkaConsumer(NotificationService notificationService, ObjectMapper objectMapper) {
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }


//    @RetryableTopic(attempts = "5", dltTopicSuffix = ".DLT", backoff = @Backoff(delay = 2000, multiplier = 2))
    // thử gửi lại 4 lần nếu vẫn thất bại gửi đến DLT topic có đuôi là .DLT,
    //backoff: retry-1: 2s, retry-2: 4s, retry-3: 8s, ...
    @KafkaListener(topics = "job_introduction", containerFactory = "kafkaListenerContainerFactory")
    public void listenGroupFoo(String message) throws JsonProcessingException, MessagingException {
        System.out.println(message);
        MessageJobIntroductionDTO messageJobIntroductionDTO = this.objectMapper.readValue(message, MessageJobIntroductionDTO.class);
        this.notificationService.sendEmailJobIntroduction(messageJobIntroductionDTO);
    }


}
