package org.example.taskflow.configurations;

import org.example.taskflow.messaging.RedisMessagePublisher;
import org.example.taskflow.messaging.RedisMessageSubscriber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisMessagingConfiguration {

        @Bean
        public RedisMessageListenerContainer redisContainer(
                RedisConnectionFactory connectionFactory,
                MessageListenerAdapter listenerAdapter) {
            RedisMessageListenerContainer container = new RedisMessageListenerContainer();
            container.setConnectionFactory(connectionFactory);
            container.addMessageListener(
                    (MessageListener) listenerAdapter, new PatternTopic("my-channel"));
            return container;
        }

        @Bean
        public MessageListenerAdapter listenerAdapter(RedisMessageSubscriber subscriber) {
            return new MessageListenerAdapter(subscriber, "onMessage");
        }

        @Bean
        public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
            RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(factory);
            return template;
        }

        @Bean
        public RedisMessagePublisher redisMessagePublisher(RedisTemplate<String, Object> redisTemplate) {
            return new RedisMessagePublisher(redisTemplate);
        }

}
