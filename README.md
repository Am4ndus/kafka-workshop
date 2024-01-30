# kafka-demo

## Tasks for Kafka Workshop

### Generic consumer and producer logic to handle messages
1. Use `PayloadReader()` and the internal method `read()` to convert String to Java object
    - https://kafka.apache.org/30/javadoc/org/apache/kafka/streams/kstream/KStream.html#mapValues(org.apache.kafka.streams.kstream.ValueMapper)
2. Use `peek()` to log the ID of incoming payloads.
    - https://kafka.apache.org/30/javadoc/org/apache/kafka/streams/kstream/KStream.html#peek(org.apache.kafka.streams.kstream.ForeachAction
3. Use `PayloadWriter()` and the internal method `write()` to convert Java object back to String and write the string to the output topic of KafkaTopics.java
    - https://kafka.apache.org/30/javadoc/org/apache/kafka/streams/kstream/KStream.html#to(java.lang.String)
4. Open KafkaApplicationTests and finish implementation of the tests using embedded kafka broker
    - open application.yml and set spring:profile:active to test
    - stop docker container while implementing test.
    
### Tasks to extend the generic setup of consumer/producer
1. Use `filter()` to filter out all records that contains name "DemoSAT". You can use PayloadData.payloadData.get(1) and send it in in the local test.
    - https://kafka.apache.org/30/javadoc/org/apache/kafka/streams/kstream/KStream.html#filter(org.apache.kafka.streams.kstream.Predicate)
2. Use `split()` to split a stream into multiple streams. All payloads containing "NASA" as a customer should be in a seperate stream. Each stream should be mapped to a different topic.
    - https://kafka.apache.org/28/javadoc/org/apache/kafka/streams/kstream/KStream.html#split()

### Tasks to add header to kafka message and use header for filtration
- 1. Add a kafkaHeader to the message inserted to the input topic. Add header "sateliteData"
   - https://kafka.apache.org/30/javadoc/org/apache/kafka/streams/kstream/KStream.html#filter(org.apache.kafka.streams.kstream.Predicate)
- 2. Create a class that implements ValueTransformerWithKey to filter out messages that does not match sateliteData. In order to do this we need to produce a message without a header.

