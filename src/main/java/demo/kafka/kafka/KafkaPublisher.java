package demo.kafka.kafka;

import demo.kafka.data.PayloadData;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
@Profile("demo")
public class KafkaPublisher {
    private final Logger log = LoggerFactory.getLogger(Integrations.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTopics kafkaTopics;
    private final Random generator = new Random();

    public KafkaPublisher(
            KafkaTemplate<String, String> kafkaTemplate,
            KafkaTopics kafkaTopics) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTopics = kafkaTopics;
    }

    @Scheduled(cron = "*/10 * * * * *")
    public void sendToTopic() {
        kafkaTemplate.send(createRecord());
    }

    private ProducerRecord<String, String> createRecord() {
        List<String> jsonData = PayloadData.payloadData;
        return new ProducerRecord<>(kafkaTopics.getInputTopic(), jsonData.get(generator.nextInt(jsonData.size())));
    }
}