package com.example.NotificationService.configurationsTest;

import com.example.NotificationService.repositories.TaskNotificationRepository;
import com.example.NotificationService.services.TaskNotificationService;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RMQConfigurationTest {

    @Bean
    public ConnectionFactory testConnectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("user");
        connectionFactory.setPassword("1234");
        return connectionFactory;
    }

    @Bean
    public MessageConverter testMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate testRabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(testConnectionFactory());
        rabbitTemplate.setConnectionFactory(testConnectionFactory());
        rabbitTemplate.setMessageConverter(testMessageConverter());
        return rabbitTemplate;
    }

}
