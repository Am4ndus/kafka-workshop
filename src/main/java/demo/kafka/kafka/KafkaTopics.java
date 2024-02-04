package demo.kafka.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaTopics {
    private final String inputTopic;
    private final String outputTopic;

    private final String darpaTopic;
    private final String nasaTopic;

    @Autowired
    public KafkaTopics(
            @Value("${kafka.topic.input.genericInputTopic}") String inputTopic,
            @Value("${kafka.topic.output.genericOutputTopic}") String outputTopic,
            @Value("${kafka.topic.output.darpaTopic}") String darpaTopic,
            @Value("${kafka.topic.output.nasaTopic}") String nasaTopic) {
        this.inputTopic = inputTopic;
        this.outputTopic = outputTopic;
        this.darpaTopic = darpaTopic;
        this.nasaTopic = nasaTopic;
    }

    public String getInputTopic() {
        return inputTopic;
    }

    public String getOutputTopic() {
        return outputTopic;
    }

    public String getDarpaTopic() {
        return darpaTopic;
    }

    public String getNasaTopic() {
        return nasaTopic;
    }
    public List<String> getAllTopics() {
        return List.of(inputTopic, outputTopic, darpaTopic, nasaTopic);
    }
}
