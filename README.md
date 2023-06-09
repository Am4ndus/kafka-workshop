# kafka-demo

## Tasks for Kafka Workshop

### Generic consumer and producer logic to handle messages
1. Use `mapValues()` and the internal method `mapToJava()` to convert String to Java object
    - https://kafka.apache.org/30/javadoc/org/apache/kafka/streams/kstream/KStream.html#mapValues(org.apache.kafka.streams.kstream.ValueMapper)
2. Use `peek()` to log the ID of incoming payloads.
    - https://kafka.apache.org/30/javadoc/org/apache/kafka/streams/kstream/KStream.html#peek(org.apache.kafka.streams.kstream.ForeachAction
3. Use `mapValues()` and the internal method `mapToJsonString()` to convert Java object back to String and write the string to the output topic of KafkaTopics.java
    - https://kafka.apache.org/30/javadoc/org/apache/kafka/streams/kstream/KStream.html#to(java.lang.String)
4. Open KafkaApplicationTests and finish implementation of the tests using embedded kafka broker
    - open application.yml and set spring:profile:active to test
    - stop docker container while implementing test.
    
### Tasks to extend the generic setup of consumer/producer
1. Use `filter()` to filter out all records that contains X
    - https://kafka.apache.org/30/javadoc/org/apache/kafka/streams/kstream/KStream.html#filter(org.apache.kafka.streams.kstream.Predicate)
2. Use `split()` to split a stream into multiple streams. All payloads containing "NASA" as a customer should be in a seperate stream. Each stream should be mapped to a different topic.
    - https://kafka.apache.org/28/javadoc/org/apache/kafka/streams/kstream/KStream.html#split()
