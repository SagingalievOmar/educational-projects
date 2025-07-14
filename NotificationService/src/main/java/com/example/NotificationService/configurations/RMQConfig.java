package com.example.NotificationService.configurations;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.Collections;

@Configuration
public class RMQConfig {

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

    @Value("${queues.notifications}")
    private String notificationsQueue;

    @Value("${queues.audit-fanout}")
    private String auditFanoutQueue;

    @Value("${queues.audit-topic}")
    private String auditTopic;

    @Value("${queues.error}")
    private String errorQueue;

    @Value("${routing-keys.dl-routing-key}")
    private String dlRoutingKey;

    @Value("${routing-keys.direct-rk}")
    private String directRK;

    @Value("${routing-keys.audit-topic}")
    private String auditTopicRK;

    @Value("${routing-keys.error-topic}")
    private String errorTopicRK;

    // exchanges
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(direct);
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(fanout);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(topic);
    }

    // dead letter exchange
    @Bean
    public DirectExchange DLExchange() {
        return new DirectExchange(DLX);
    }

    // queues
    @Bean
    public Queue notificationsQueue() {
        Queue queue = new Queue(notificationsQueue);
        queue.addArgument("x-dead-letter-exchange", DLX);
        queue.addArgument("x-dead-letter-routing-key", dlRoutingKey);
        return queue;
    }

    @Bean
    public Queue auditFanoutQueue() {
        Queue queue = new Queue(auditFanoutQueue);
        queue.addArgument("x-dead-letter-exchange", DLX);
        queue.addArgument("x-dead-letter-routing-key", dlRoutingKey);
        return queue;
    }

    @Bean
    public Queue auditTopicQueue() {
        Queue queue = new Queue(auditTopic);
        queue.addArgument("x-dead-letter-exchange", DLX);
        queue.addArgument("x-dead-letter-routing-key", dlRoutingKey);
        return queue;
    }

    @Bean
    public Queue errorQueue() {
        Queue queue = new Queue(errorQueue);
        queue.addArgument("x-dead-letter-exchange", DLX);
        queue.addArgument("x-dead-letter-routing-key", dlRoutingKey);
        return queue;
    }

    // bindings
    @Bean
    public Binding notificationBinding() {
        return BindingBuilder
                .bind(notificationsQueue())
                .to(directExchange()).with(directRK);
    }

    @Bean
    public Binding auditFanoutBinding() {
        return BindingBuilder
                .bind(auditFanoutQueue())
                .to(fanoutExchange());
    }

    @Bean
    public Binding auditTopicBinding() {
        return BindingBuilder
                .bind(auditTopicQueue())
                .to(topicExchange())
                .with(auditTopicRK);
    }

    @Bean
    public Binding errorBinding() {
        return BindingBuilder
                .bind(errorQueue())
                .to(topicExchange())
                .with(errorTopicRK);
    }

    // rest
    @Bean
    public MessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages("*");
        typeMapper.setIdClassMapping(Collections.emptyMap());
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate();
        template.setConnectionFactory(connectionFactory());
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        RetryPolicy retryPolicy = new SimpleRetryPolicy(1);
        retryTemplate.setRetryPolicy(retryPolicy);

        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(1000);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        return retryTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setMessageConverter(messageConverter());
        factory.setDefaultRequeueRejected(false);
        factory.setConnectionFactory(connectionFactory());
        factory.setAdviceChain(
                RetryInterceptorBuilder
                        .stateless()
                        .retryOperations(retryTemplate())
                        .recoverer(new RepublishMessageRecoverer(
                                rabbitTemplate(), DLX, dlRoutingKey
                        )).build());
        return factory;
    }
}
