package org.example.taskflow.configurations;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RMQConfiguration {

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${exchanges.direct}")
    private String direct;

    @Value("${exchanges.fanout}")
    private String fanout;

    @Value("${exchanges.topic}")
    private String topic;

    @Value("${exchanges.DLX}")
    private String DLX;

    @Value("${queues.DLQ}")
    private String DLQ;

    @Value("${routing-keys.dl-routing-key}")
    private String dlRoutingKey;

    // exchanges
    @Bean
    public DirectExchange directExchange() {
        DirectExchange exchange = new DirectExchange(direct);
        rabbitAdmin(connectionFactory()).declareExchange(exchange);
        return exchange;
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        FanoutExchange exchange = new FanoutExchange(fanout);
        rabbitAdmin(connectionFactory()).declareExchange(exchange);
        return exchange;
    }

    @Bean
    public TopicExchange topicExchange() {
        TopicExchange exchange = new TopicExchange(topic);
        rabbitAdmin(connectionFactory()).declareExchange(exchange);
        return exchange;
    }

    //dead letter exchange
    @Bean
    public DirectExchange DLExchange() {
        DirectExchange exchange = new DirectExchange(DLX);;
        rabbitAdmin(connectionFactory()).declareExchange(exchange);
        return exchange;
    }

    // dead letter queue
    @Bean
    public Queue DLQueue() {
        return new Queue(DLQ);
    }

    // dead letter bindings
    @Bean
    public Binding DLBinding() {
        return BindingBuilder
                .bind(DLQueue())
                .to(DLExchange())
                .with(dlRoutingKey);
    }

    // rest
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setMessageConverter(messageConverter());
        rabbitTemplate.setConnectionFactory(connectionFactory);
        return rabbitTemplate;
    }

}
