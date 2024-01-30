package demo.kafka.config;

import demo.kafka.kafka.support.PayloadReader;
import demo.kafka.kafka.support.PayloadWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

/**
 * Spring @Configuration annotation is part of the spring core framework.
 * Spring Configuration annotation indicates that the class has @Bean definition methods.
 * So Spring container can process the class and generate Spring Beans to be used in the application.
 */
@Configuration
public class CommonConfig {
    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("Europe/Oslo"));
    }

    @Bean
    public PayloadReader payloadReader() {
        return new PayloadReader();
    }

    @Bean
    public PayloadWriter payloadWriter() {
        return new PayloadWriter();
    }
}
