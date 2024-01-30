package demo.kafka;

import demo.kafka.data.PayloadData;
import demo.kafka.kafka.KafkaTopics;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

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
	void receiveAndSendMessage(){
		//given
		// a string message that can be serialized to a Payload Object
		String payloadJson = PayloadData.payloadData.get(0);

		//when
		//the message is published to kafka topic that our application consumed from
		kafkaTestSupport.sendKafkaMessage(kafkaTopics.getInputTopic(), payloadJson);

		//then
		//a message is produced to a kafka topic that our application produces too
		String outputMessage = kafkaTestSupport.readKafkaMessage(kafkaTopics.getOutputTopic());

		//...and we can do our assertions.
		assertThat(outputMessage).isNotNull();
	}
}
