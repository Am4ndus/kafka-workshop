package demo.kafka.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import demo.kafka.data.Payload;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Integrations {
    private final KafkaTopics kafkaTopics;
    private final Logger log = LoggerFactory.getLogger(Integrations.class);
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public Integrations(
            KafkaTopics kafkaTopics) {
        this.kafkaTopics = kafkaTopics;
    }

    @Bean
    public KStream<String, String> buildStreams(StreamsBuilder kStreamsBuilder) {
        KStream<String, String> inputData = kStreamsBuilder.stream(kafkaTopics.getInputTopic());
        inputData
                .mapValues(this::mapToJava)
                .peek((key, payload) -> log.info("Message received with id: {} and customer: {}", payload.id, payload.customers.size() > 0 ? payload.customers.get(0) : "unknown"))
                .mapValues(this::mapToJsonString)
                .to(kafkaTopics.getOutputTopic());

        printTopology(kStreamsBuilder);
        return inputData;
    }

    private void printTopology(StreamsBuilder kStreamsBuilder) {
        Topology topology = kStreamsBuilder.build();
        log.info(topology.describe().toString());
    }

    private Payload mapToJava(String message) {
        try {
            return mapper.readValue(message, Payload.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String mapToJsonString(Payload payload) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
