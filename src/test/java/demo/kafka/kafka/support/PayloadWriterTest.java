package demo.kafka.kafka.support;

import demo.kafka.data.model.Payload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
class PayloadWriterTest {

    private PayloadWriter testSubject;
    @BeforeEach
    void setup() {
        testSubject = new PayloadWriter();
    }

    @Test
    void shouldParseValidMessage(){
        //given
        //A valid JSON message is sent
        Payload payloadMessage = new PayloadReader().read(getValidJson());

        //when
        //The test subject (reader) reads the message
        String payload = testSubject.write(payloadMessage);

        //then
        //Assert that the payload was read
        assertThat(payload).isNotNull();
    }

    private String getValidJson(){
        return "{\n" +
                "  \"dragon\": {\n" +
                "    \"capsule\": null,\n" +
                "    \"mass_returned_kg\": null,\n" +
                "    \"mass_returned_lbs\": null,\n" +
                "    \"flight_time_sec\": null,\n" +
                "    \"manifest\": null,\n" +
                "    \"water_landing\": null,\n" +
                "    \"land_landing\": null\n" +
                "  },\n" +
                "  \"name\": \"Orbcomm-OG2-M2\",\n" +
                "  \"type\": \"Satellite\",\n" +
                "  \"reused\": false,\n" +
                "  \"launch\": \"5eb87cefffd86e000604b342\",\n" +
                "  \"customers\": [\n" +
                "    \"Orbcomm\"\n" +
                "  ],\n" +
                "  \"norad_ids\": [\n" +
                "    41187\n" +
                "  ],\n" +
                "  \"nationalities\": [\n" +
                "    \"UnitedStates\"\n" +
                "  ],\n" +
                "  \"manufacturers\": [\n" +
                "    \"Boeing\"\n" +
                "  ],\n" +
                "  \"mass_kg\": 2034,\n" +
                "  \"mass_lbs\": 4484,\n" +
                "  \"orbit\": \"LEO\",\n" +
                "  \"reference_system\": \"geocentric\",\n" +
                "  \"regime\": \"low-earth\",\n" +
                "  \"longitude\": null,\n" +
                "  \"semi_major_axis_km\": 7087.639,\n" +
                "  \"eccentricity\": 0.0002106,\n" +
                "  \"periapsis_km\": 708.011,\n" +
                "  \"apoapsis_km\": 710.996,\n" +
                "  \"inclination_deg\": 47.0036,\n" +
                "  \"period_min\": 98.972,\n" +
                "  \"lifespan_years\": 5,\n" +
                "  \"epoch\": \"2022-01-14T07:18:16.000Z\",\n" +
                "  \"mean_motion\": 14.54957858,\n" +
                "  \"raan\": 230.8503,\n" +
                "  \"arg_of_pericenter\": 340.4453,\n" +
                "  \"mean_anomaly\": 19.635,\n" +
                "  \"id\": \"5eb0e4beb6c3bb0006eeb1fd\"\n" +
                "}";
    }

}