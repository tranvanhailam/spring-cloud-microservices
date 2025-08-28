package vnpt_it.vn.notificationservice.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${spring.kafka.consumer.group-id}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                groupId);
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

//    @Bean
//    public DefaultErrorHandler errorHandler(KafkaTemplate<String, String> kafkaTemplate) {
//        // phân phối lại event sau mỗi 1s, gửi lại 2 lần
//        FixedBackOff backOff = new FixedBackOff(1000L, 2L); // 2 lần retry
//        //Nếu vẫn lỗi gửi event bị lỗi vào trong create_company.DLT topic do kafka tự tạo
//        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
//                kafkaTemplate,
//                (record, ex) -> new TopicPartition(record.topic() + ".DLT", record.partition())
//        );
//        return new DefaultErrorHandler(recoverer, backOff);
//    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String>
    kafkaListenerContainerFactory(KafkaTemplate<String, String> kafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
//        factory.setCommonErrorHandler(errorHandler(kafkaTemplate));
        return factory;
    }
}
