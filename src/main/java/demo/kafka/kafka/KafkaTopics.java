package demo.kafka.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaTopics {
    private final String inputTopic;
    private final String outputTopic;

    @Autowired
    public KafkaTopics(
            @Value("${kafka.topic.input.genericInputTopic}") String inputTopic,
            @Value("${kafka.topic.output.genericOutputTopic}") String outputTopic) {
        this.inputTopic = inputTopic;
        this.outputTopic = outputTopic;
    }

    public String getInputTopic() {
        return inputTopic;
    }

    public String getOutputTopic() {
        return outputTopic;
    }

    public List<String> getAllTopics() {
        return List.of(inputTopic, outputTopic);
    }
}
