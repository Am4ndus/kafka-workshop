package demo.kafka.kafka;

import demo.kafka.kafka.support.PayloadReader;
import demo.kafka.kafka.support.PayloadWriter;
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
    public KStream<String, String> buildStreams(StreamsBuilder kStreamsBuilder) {
        KStream<String, String> inputData = kStreamsBuilder.stream(kafkaTopics.getInputTopic());
        inputData
                .mapValues(payloadReader::read)
                .mapValues(payloadWriter::write)
                .to(kafkaTopics.getOutputTopic());
        return inputData;
    }

    private void printTopology(StreamsBuilder kStreamsBuilder) {
        Topology topology = kStreamsBuilder.build();
        log.info(topology.describe().toString());
    }
}
