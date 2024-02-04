package demo.kafka;

import demo.kafka.data.PayloadData;
import demo.kafka.kafka.KafkaTopics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureObservability
@SpringBootTest
class KafkaApplicationTests {
	protected final KafkaTestSupport kafkaTestSupport;
	protected final KafkaTopics kafkaTopics;
	private final String darpaMessageWithNameFalconSAT2 = PayloadData.payloadData.get(0);
	private final String darpaMessageWithNameDemoSAT = PayloadData.payloadData.get(1);
	private final String nasaMessage = PayloadData.payloadData.get(2);
	private final String orsMessage = PayloadData.payloadData.get(3);

	@Autowired
	KafkaApplicationTests(KafkaTestSupport kafkaTestSupport, KafkaTopics kafkaTopics){
		this.kafkaTestSupport = kafkaTestSupport;
		this.kafkaTopics = kafkaTopics;
	}

	@Test
	void receiveAndSendMessage(){
		//given
		// a string message that can be serialized to a Payload Object

		//when
		//the message is published to kafka topic that our application consumed from
		kafkaTestSupport.sendKafkaMessage(kafkaTopics.getInputTopic(), darpaMessageWithNameFalconSAT2);

		//then
		//a message is produced to a kafka topic that our application produces too
		String outputMessage = kafkaTestSupport.readKafkaMessage(kafkaTopics.getDarpaTopic());

		//...and we can do our assertions.
		assertThat(outputMessage).isNotNull();
	}

	@Test
	void shouldFilterOutDemoSat(){
		//given
		// a string message that can be serialized to a Payload Object

		//when
		//the message is published to kafka topic that our application consumed from
		kafkaTestSupport.sendKafkaMessage(kafkaTopics.getInputTopic(), darpaMessageWithNameDemoSAT);

		assertThrows(IllegalStateException.class, () -> {
			kafkaTestSupport.readKafkaMessage(kafkaTopics.getDarpaTopic());
		});
	}

	//1. Go to the kStream and identify a way to split the stream into separate streams based on customer name
	//2. add new topics in the application.yml
	//3. include the new topics in the KafkaTopic class
	//4. add the new topics to the consumeFromEmbeddedTopics function in KafkaTestSupport.
	//5. create a test that published payloadData.get(0) = darpa and payloadData.get(1) = nasa and verify
	//that the messages are sent to the new topics.
	@Test
	void splitInputStreamAndPublishMessagesOnTwoNewTopic(){
		kafkaTestSupport.sendKafkaMessage(kafkaTopics.getInputTopic(), darpaMessageWithNameFalconSAT2);
		kafkaTestSupport.sendKafkaMessage(kafkaTopics.getInputTopic(), darpaMessageWithNameDemoSAT); //will be filtered out
		kafkaTestSupport.sendKafkaMessage(kafkaTopics.getInputTopic(), nasaMessage);
		kafkaTestSupport.sendKafkaMessage(kafkaTopics.getInputTopic(), orsMessage);

		String nasaOutputMessage = kafkaTestSupport.readKafkaMessage(kafkaTopics.getNasaTopic());
		String darpaOutputMessage = kafkaTestSupport.readKafkaMessage(kafkaTopics.getDarpaTopic());
		String defaultOutputMessage = kafkaTestSupport.readKafkaMessage(kafkaTopics.getOutputTopic());

		assertThat(nasaOutputMessage).isNotNull();
		assertThat(darpaOutputMessage).isNotNull();
		assertThat(defaultOutputMessage).isNotNull();
	}
}
