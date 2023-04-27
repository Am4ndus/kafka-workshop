package demo.kafka;

import demo.kafka.data.PayloadData;
import demo.kafka.kafka.KafkaTopics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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

	//Hint 1: data.get(2) inneholder Nasa som kunde
	//Hint 2: Bruk kafkaTestSupport APIet til å sende melding med gitt data til input topicet
	//Hint 3: kafkaTestSupport støtter ikke lese mer enn 1 melding fra topicet. Derfor må vi lese output før vi skriver ny melding til input
	@Test
	void sendThreeMessagesToKafkaTopicAndValidateThatDataWhereCustomersIncludeNasaIsFiltereOut() {
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

	//Hint 1: Nå har vi flere input topics. Kan vi verifisere at meldingene blir sendt til riktig output topic?
	//Hint 2: Følg samme strukturen som i testen over.
	@Test
	void verifySplit() {

	}
}
