package demo.kafka.kafka;

import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
@Profile("demo")
public class TestEnvironmentKafkaTopicCreator {
    private final KafkaTopics kafkaTopics;
    private final KafkaAdmin kafkaAdmin;

    public TestEnvironmentKafkaTopicCreator(KafkaTopics kafkaTopics, KafkaAdmin kafkaAdmin) {
        this.kafkaTopics = kafkaTopics;
        this.kafkaAdmin = kafkaAdmin;
    }

    @PostConstruct
    void createMissingTopics() {
        AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties());
        KafkaTopicCreator kafkaTopicCreator = new KafkaTopicCreator(adminClient, kafkaTopics.getAllTopics());
        kafkaTopicCreator.createMissingTopics();
    }
}
