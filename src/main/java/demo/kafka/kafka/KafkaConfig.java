package demo.kafka.kafka;

import demo.kafka.kafka.support.KafkaStreamsStatus;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.core.CleanupConfig;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 This class has the configuration properties of the Kafka Streams parameters.
 It defines the KafkaStreamsConfiguration that tells the application the kafka host to listen to.
 StreamsBuilderFactoryBean is defined to create a en streams-topology
 The starting point is the kStream() that reads data from the source topic and hands out processing to
 other processors.
 */
@Configuration
public class KafkaConfig {
    private final Map<String, Object> kafkaProperties;
    private final String bootstrapServers;

    public KafkaConfig(
            @Value("${kafka.bootstrap-servers}") String bootstrapServer,
            @Value("${kafka.group_id}") String groupId) {
        this.kafkaProperties = new HashMap<>();
        this.bootstrapServers = bootstrapServer;
        this.kafkaProperties.put(StreamsConfig.APPLICATION_ID_CONFIG, groupId);
        this.kafkaProperties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        this.kafkaProperties.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 1000);
        this.kafkaProperties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass()); //defines how messages will be serialized
        this.kafkaProperties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        return new KafkaAdmin(kafkaProperties);
    }

    @Bean
    public StreamsBuilderFactoryBean kafkaStreamsBuilder() {
        return new StreamsBuilderFactoryBean(new KafkaStreamsConfiguration(kafkaProperties),
                new CleanupConfig(true, false));
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        Map<String, Object> producerConfig = new HashMap<>();
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerConfig));
    }

    @Bean
    public HealthIndicator kafkaStreamsHealthIndicator(StreamsBuilderFactoryBean streamsBuilder) {
        return () -> {
            Optional<KafkaStreams.State> optionalErrorState = new KafkaStreamsStatus(streamsBuilder::getKafkaStreams).errorState();
            return optionalErrorState
                    .map(errorState -> Health.down().withDetail("state", errorState.name()).build())
                    .orElseGet(() -> Health.up().build());
        };
    }
}
