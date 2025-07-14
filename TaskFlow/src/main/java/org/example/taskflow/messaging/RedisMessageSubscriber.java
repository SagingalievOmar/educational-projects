package org.example.taskflow.messaging;


import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class RedisMessageSubscriber implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        String channel = new String(pattern, StandardCharsets.UTF_8);
        System.out.printf("Получено сообщение из канала [%s]: %s%n", channel, body);
    }
}
