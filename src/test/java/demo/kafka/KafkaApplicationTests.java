package demo.kafka;

import demo.kafka.data.PayloadData;
import demo.kafka.kafka.KafkaTopics;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureObservability
@SpringBootTest
class KafkaApplicationTests {
	protected final KafkaTestSupport kafkaTestSupport;
	private final String darpaMessageWithNameFalconSAT2 = PayloadData.payloadData.get(0);
	private final String darpaMessageWithNameDemoSAT = PayloadData.payloadData.get(1);
	private final String nasaMessage = PayloadData.payloadData.get(2);
	private final String orsMessage = PayloadData.payloadData.get(3);
	protected final KafkaTopics kafkaTopics;
	@Autowired
	KafkaApplicationTests(KafkaTestSupport kafkaTestSupport, KafkaTopics kafkaTopics){
		this.kafkaTestSupport = kafkaTestSupport;
		this.kafkaTopics = kafkaTopics;
	}

	@Test
	void receiveAndSendMessage(){
	/*	1. Use `PayloadReader()` and the internal method `read()` to convert String to Java object
		- https://kafka.apache.org/30/javadoc/org/apache/kafka/streams/kstream/KStream.html#mapValues(org.apache.kafka.streams.kstream.ValueMapper)
		2. Use `peek()` to log the ID of incoming payloads.
				- https://kafka.apache.org/30/javadoc/org/apache/kafka/streams/kstream/KStream.html#peek(org.apache.kafka.streams.kstream.ForeachAction
		3. Use `PayloadWriter()` and the internal method `write()` to convert Java object back to String and write the string to the output topic of KafkaTopics.java
				- https://kafka.apache.org/30/javadoc/org/apache/kafka/streams/kstream/KStream.html#to(java.lang.String)
		4. Open KafkaApplicationTests and finish implementation of the tests using embedded kafka broker
		- open application.yml and set spring:profile:active to test. This will use the embedded kafka-broker instead of the one in docker.
		- stop docker container while running test.*/
	}

	@Test
	void splitInputStreamAndPublishMessagesOnTwoNewTopic(){
/*		2:0 Tasks to extend the generic setup of consumer/producer
		1. Use `filter()` to filter out all records that contains the name "DemoSAT" in the Customer array. You can use PayloadData.payloadData.get(1) and send it in the local test.
				- https://kafka.apache.org/30/javadoc/org/apache/kafka/streams/kstream/KStream.html#filter(org.apache.kafka.streams.kstream.Predicate)
		2. Use `split()` to split a stream into multiple streams. All payloads containing "NASA" or "DARPA" as a customer should be in a seperate stream. Each stream should be mapped to a different topic.
				- https://kafka.apache.org/28/javadoc/org/apache/kafka/streams/kstream/KStream.html#split()*/
	}
}
