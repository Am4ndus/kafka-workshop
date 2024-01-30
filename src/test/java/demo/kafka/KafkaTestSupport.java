package demo.kafka;

import demo.kafka.kafka.KafkaTopics;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.assertj.core.util.Streams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class KafkaTestSupport {

    private EmbeddedKafkaBroker embeddedKafkaBroker;
    private KafkaTemplate<String, String> kafkaProducer;
    private Consumer<String, String> kafkaConsumer;

    @Autowired
    private KafkaTopics kafkaTopics;

    @PostConstruct
    private void setup(){
        embeddedKafkaBroker = new EmbeddedKafkaBroker(1, false, 1, kafkaTopics.getAllTopics().toArray(String[]::new))
                .brokerProperty("log.cleaner.disabled", "false")
                .brokerProperty("log.message.timestamp.type", "LogAppendTime");
        embeddedKafkaBroker.afterPropertiesSet();

        System.setProperty("kafka.bootstrap_servers", embeddedKafkaBroker.getBrokersAsString());

        Map<String, Object> configs = KafkaTestUtils.consumerProps("kafkaGroupI", "false", embeddedKafkaBroker);
        kafkaConsumer = new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new StringDeserializer()).createConsumer();

        embeddedKafkaBroker.consumeFromEmbeddedTopics(
                kafkaConsumer,
                kafkaTopics.getInputTopic(),
                kafkaTopics.getOutputTopic()
        );

        kafkaProducer = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(KafkaTestUtils.producerProps(embeddedKafkaBroker)));
    }

    void sendKafkaMessage(String topic, String message, Header... headers){
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
        Arrays.stream(headers).forEach(header -> record.headers().add(header));
        kafkaProducer.send(record);
        kafkaProducer.flush();
    }

    String readKafkaMessage(String topic){
        ConsumerRecord<String, String> singleRecord = KafkaTestUtils.getSingleRecord(kafkaConsumer, topic, Duration.ofSeconds(3));
        return singleRecord != null ? singleRecord.value() : null;
    }

    Map<String, List<String>> readKafkaMessages(int numberOfMessages){
        return Streams.stream(KafkaTestUtils.getRecords(kafkaConsumer, Duration.ofSeconds(3), numberOfMessages))
                .collect(Collectors.groupingBy(ConsumerRecord::topic, Collectors.mapping(ConsumerRecord::value, Collectors.toList())));
    }
}
