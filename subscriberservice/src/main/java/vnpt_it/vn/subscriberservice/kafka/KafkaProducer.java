package vnpt_it.vn.subscriberservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import vnpt_it.vn.subscriberservice.domain.MessageJobIntroductionDTO;

@Service
public class KafkaProducer {
    private KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessageJobIntroduction(MessageJobIntroductionDTO messageJobIntroductionDTO) {

        kafkaTemplate.send("job_introduction",messageJobIntroductionDTO.getToEmail(), messageJobIntroductionDTO).whenComplete((result, e) -> {
            if (e == null) {
                System.out.println(result.getRecordMetadata().partition());
            }else {
                e.printStackTrace();
            }
        });
    }


}
