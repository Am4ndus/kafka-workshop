package demo.kafka.kafka.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.kafka.data.model.Payload;

public class PayloadReader {

    private final ObjectMapper mapper = new ObjectMapper();

    public Payload read(String message){
        return mapToJava(message);
    }

    private Payload mapToJava(String message) {
        try {
            return mapper.readValue(message, Payload.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
