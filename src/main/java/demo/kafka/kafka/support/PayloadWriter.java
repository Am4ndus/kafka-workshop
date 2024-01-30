package demo.kafka.kafka.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import demo.kafka.data.model.Payload;

public class PayloadWriter {

    public String write(Payload payload){
        return mapToJsonString(payload);
    }

    private String mapToJsonString(Payload payload) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
