package com.ripple.friend.service;

import com.ripple.friend.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

/**
 * @author 花海
 * @date 2023/12/3
 * @description
 */

@SpringBootTest
public class RedisTest {

    @Resource
    private RedisTemplate redisTemplate;

    @Test
    void test() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("bobo1", "啵啵");
        valueOperations.set("boboInt", 1);
        User user = new User();
        user.setId(1L);
        user.setUsername("ripple");
        valueOperations.set("ripple", user);
    }
}
