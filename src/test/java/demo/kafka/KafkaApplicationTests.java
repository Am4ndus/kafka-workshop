package demo.kafka;

import demo.kafka.data.PayloadData;
import demo.kafka.kafka.KafkaTopics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@AutoConfigureObservability
@SpringBootTest
class KafkaApplicationTests {

	protected final KafkaTestSupport kafkaTestSupport;

	protected final KafkaTopics kafkaTopics;

	@Autowired
	KafkaApplicationTests(KafkaTestSupport kafkaTestSupport, KafkaTopics kafkaTopics){
		this.kafkaTestSupport = kafkaTestSupport;
		this.kafkaTopics = kafkaTopics;
	}

	@Test
	void verifyFiltering() {
		kafkaTestSupport.sendKafkaMessage(kafkaTopics.getInputTopic(), PayloadData.payloadData.get(0));
		String darpa1 = kafkaTestSupport.readKafkaMessage(kafkaTopics.getOutputTopic());

		kafkaTestSupport.sendKafkaMessage(kafkaTopics.getInputTopic(), PayloadData.payloadData.get(1));
		String darpa2 = kafkaTestSupport.readKafkaMessage(kafkaTopics.getOutputTopic());

		kafkaTestSupport.sendKafkaMessage(kafkaTopics.getInputTopic(), PayloadData.payloadData.get(2));
		Throwable throwOutputTopic = catchThrowable (() -> kafkaTestSupport.readKafkaMessage(kafkaTopics.getOutputTopic()));

		assertThat(darpa1).isNotEmpty();
		assertThat(darpa2).isNotEmpty();
		assertThat(throwOutputTopic).isInstanceOf(IllegalStateException.class)
				.hasMessageContaining("No records found for topic");
	}

	@Test
	void verifySplit() {
		kafkaTestSupport.sendKafkaMessage(kafkaTopics.getInputTopic(), PayloadData.payloadData.get(0));
		String darpa1 = kafkaTestSupport.readKafkaMessage(kafkaTopics.getDarpaTopic());

		kafkaTestSupport.sendKafkaMessage(kafkaTopics.getInputTopic(), PayloadData.payloadData.get(2));
		String nasa = kafkaTestSupport.readKafkaMessage(kafkaTopics.getNasaTopic());
		assertThat(darpa1).isNotEmpty();
		assertThat(nasa).isNotEmpty();
	}
}
