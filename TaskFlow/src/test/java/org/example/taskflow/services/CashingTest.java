package org.example.taskflow.services;

import org.example.taskflow.DTO.responces.UserResponse;
import org.example.taskflow.models.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;

@SpringBootTest
public class CashingTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Test
    public void firstGetFromDBSecondGetFromRedis() {
        userService.getUserResponse(11L);
        Object value = redisTemplate.opsForValue().get("users::11");
        System.out.println(value);
        Assertions.assertNotNull(value);
    }

    @Test
    void getUserAndSaveItInRedisThenDeleteAndGetItFromRedis() {
        userService.getUserResponse(11L);
        Object value = redisTemplate.opsForValue().get("users::11");
        System.out.println(value);
        Assertions.assertNotNull(value);
        redisTemplate.delete("users::11");
        Object value2 = redisTemplate.opsForValue().get("users::12");
        Assertions.assertNull(value2);
    }

    @Test
    void getUserAndSaveItInRedisWaitCoupleOfMinutesThenGetItFromRedisForTestTTl() throws InterruptedException {
        userService.getUserResponse(11L);
        Object value = redisTemplate.opsForValue().get("users::11");
        Assertions.assertNotNull(value);
        Thread.sleep(2000);
        Object value2 = redisTemplate.opsForValue().get("users::12");
        System.out.println(value2);
        Assertions.assertNull(value2);
    }
}
