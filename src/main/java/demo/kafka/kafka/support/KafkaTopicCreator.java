package demo.kafka.kafka.support;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaTopicCreator {
    private static final Logger log = LoggerFactory.getLogger(KafkaTopicCreator.class);
    private final List<String> kafkaTopics;
    private final AdminClient adminClient;
    private final int timeoutSeconds;

    public KafkaTopicCreator(AdminClient adminClient, List<String> kafkaTopics) {
        this(adminClient, kafkaTopics, 120);
    }

    public KafkaTopicCreator(AdminClient adminClient, List<String> kafkaTopics, int timeoutSeconds) {
        this.kafkaTopics = kafkaTopics;
        this.adminClient = adminClient;
        this.timeoutSeconds = timeoutSeconds;
    }

    public void createMissingTopics() {
        KafkaBrokerFacade kafkaBrokerFacade = new KafkaBrokerFacade(adminClient, timeoutSeconds);
        List<String> missingTopics = kafkaTopics.stream()
                .filter(topic -> !kafkaBrokerFacade.topicExists(topic))
                .collect(toList());

        log.info("Creating topics: {}", missingTopics);
        kafkaBrokerFacade.createTopics(missingTopics);
    }

    private static class KafkaBrokerFacade {
        private final AdminClient adminClient;
        private final Set<String> existingTopics;

        KafkaBrokerFacade(AdminClient adminClient, int timeoutSeconds) {
            this.adminClient = adminClient;
            this.existingTopics = readAllTopics(adminClient, timeoutSeconds).stream().map(TopicListing::name).collect(Collectors.toSet());
        }

        boolean topicExists(String topicName) {
            return existingTopics.contains(topicName);
        }

        void createTopics(List<String> topicsNames) {
            adminClient.createTopics(topicsNames.stream().map(this::toNewTopic).collect(toList()));
        }

        private Collection<TopicListing> readAllTopics(AdminClient adminClient, int timeoutSeconds) {
            try {
                return adminClient.listTopics().listings().get(timeoutSeconds, TimeUnit.SECONDS);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        private NewTopic toNewTopic(String topicName) {
            return new NewTopic(topicName, 1, (short) 1);
        }
    }
}
