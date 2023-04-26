package demo.kafka.kafka.support;

import java.util.Optional;
import java.util.function.Supplier;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KafkaStreams.State;

public class KafkaStreamsStatus {
    private final Supplier<KafkaStreams> kafkaStreamsSupplier;

    public KafkaStreamsStatus(Supplier<KafkaStreams> kafkaStreamsSupplier) {
        this.kafkaStreamsSupplier = kafkaStreamsSupplier;
    }

    public Optional<State> errorState() {
        State kafkaStreamsState = ((KafkaStreams)this.kafkaStreamsSupplier.get()).state();
        return kafkaStreamsState != State.CREATED && !kafkaStreamsState.isRunningOrRebalancing() ? Optional.of(kafkaStreamsState) : Optional.empty();
    }
}