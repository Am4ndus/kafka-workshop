package demo.kafka.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaTopics {
    private final String inputTopic;
    private final String outputTopic;

    private final String darpa;

    private final String nasa;

    @Autowired
    public KafkaTopics(
            @Value("${kafka.topic.input.genericInputTopic}") String inputTopic,
            @Value("${kafka.topic.output.genericOutputTopic}") String outputTopic,
            @Value("${kafka.topic.output.nasa}") String nasa,
            @Value("${kafka.topic.output.darpa}") String darpa) {
        this.inputTopic = inputTopic;
        this.outputTopic = outputTopic;
        this.nasa = nasa;
        this.darpa = darpa;
    }

    public String getInputTopic() {
        return inputTopic;
    }

    public String getOutputTopic() {
        return outputTopic;
    }

    public List<String> getAllTopics() {
        return List.of(inputTopic, outputTopic, nasa, darpa);
    }
}
