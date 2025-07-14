package org.example.taskflow.configurations;

import lombok.RequiredArgsConstructor;
import org.example.taskflow.Aspects.LoggingAspect;
import org.example.taskflow.messaging.KafkaProducer;
import org.example.taskflow.repositories.RecordRepositories.LogEntryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
@RequiredArgsConstructor
public class AspectConfiguration {

    private final KafkaProducer kafkaProducer;
    private final LogEntryRepository logEntryRepository;

    @Bean
    public LoggingAspect loggingAspect() {
        return new LoggingAspect(kafkaProducer, logEntryRepository);
    }

}
