package vnpt_it.vn.notificationservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopic {
    @Bean
    public NewTopic job_introduction() {
        return new NewTopic("job_introduction", 3, (short) 3);
    }
}
