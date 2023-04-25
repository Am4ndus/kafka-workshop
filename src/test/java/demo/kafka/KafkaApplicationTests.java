package demo.kafka;

import demo.kafka.data.PayloadData;
import demo.kafka.kafka.KafkaTopics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
		List<String> data = PayloadData.payloadData; //inneholder liste med json data

	}

	//Hint 1: Nå har vi flere input topics. Kan vi verifisere at meldingene blir sendt til riktig output topic?
	//Hint 2: Følg samme strukturen som i testen over.
	@Test
	void verifySplit() {

	}
}
