package demo.kafka.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import demo.kafka.data.model.Payload;
import demo.kafka.kafka.support.PayloadReader;
import demo.kafka.kafka.support.PayloadWriter;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class Integrations {
    private final KafkaTopics kafkaTopics;
    private final PayloadReader payloadReader;
    private final PayloadWriter payloadWriter;
    private final Logger log = LoggerFactory.getLogger(Integrations.class);

    @Autowired
    public Integrations(
            KafkaTopics kafkaTopics,
            PayloadReader payloadReader,
            PayloadWriter payloadWriter) {
        this.kafkaTopics = kafkaTopics;
        this.payloadReader = payloadReader;
        this.payloadWriter = payloadWriter;
    }

    //Application topology
    //KStream is an abstraction of a record stream of KeyValue pairs, i.e.,
    // each record is an independent entity/event in the real world
    @Bean
    public KStream<String, Payload> buildStreams(StreamsBuilder kStreamsBuilder) {

        //creating input stream that holds the data consumed from the kafka-topic directly (string key and value)
        KStream<String, String> inputStream = kStreamsBuilder.stream(kafkaTopics.getInputTopic());

        //1:0
        //creating a stream of type key: String, value: Payload that handles object mapping and message reception logging
        KStream<String, Payload> payloadStream = inputStream
                .mapValues(payloadReader::read)
                .peek((key, value) -> log.info("Received message with id {} from topic {}", value.id, kafkaTopics.getInputTopic()));
        //2:0
        //creating a stream branch that routes each element in the payloadStream to a topic based on a filter expression.
        payloadStream
                .filter((s, payload) -> !Objects.equals(payload.name, "DemoSAT"))
                .split()
                .branch((key, payLoad) -> payLoad.customers.contains("NASA"),
                        Branched.withConsumer(ks -> ks
                                .peek((key, value) -> log.info("Sending message with customer NASA to topic {}", kafkaTopics.getNasaTopic()))
                                .mapValues(payloadWriter::write)
                                .to(kafkaTopics.getNasaTopic())))
                .branch((key, payLoad) -> payLoad.customers.contains("DARPA"),
                        Branched.withConsumer(ks -> ks
                                .peek((key, value) -> log.info("Sending message with customer DARPA to topic {}", kafkaTopics.getDarpaTopic()))
                                .mapValues(payloadWriter::write)
                                .to(kafkaTopics.getDarpaTopic()))
                )
                .branch((key, payLoad) -> true,
                        Branched.withConsumer(ks -> ks
                                .peek((key, value) -> log.info("Sending message with non-supported customer to topic {}", kafkaTopics.getOutputTopic()))
                                .mapValues(payloadWriter::write)
                                .to(kafkaTopics.getOutputTopic()))
                );

        return payloadStream;
    }

    private void printTopology(StreamsBuilder kStreamsBuilder) {
        Topology topology = kStreamsBuilder.build();
        log.info(topology.describe().toString());
    }
}
