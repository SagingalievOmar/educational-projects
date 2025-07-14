package org.example.taskflow.configurations;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic logsTopic() {
        return TopicBuilder
                .name("taskFlow.logs")
                .partitions(1)
                .build();
    }

}
