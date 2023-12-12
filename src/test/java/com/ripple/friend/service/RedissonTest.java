package com.ripple.friend.service;

import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @description
 */

@SpringBootTest
public class RedissonTest {

    // Redisson 客户端，用于操作本地 redis
    @Resource
    private RedissonClient redissonClient;

    @Test
    void test() {
        // 1. list数据存在本地 JVM 内存中
        List<String> list = new ArrayList<>();
        list.add("bobo");
        list.get(0);
        list.remove(0);

        // 2. 数据存在 redis 中 k-v
        RList<Object> rList = redissonClient.getList("test-list");
        rList.add("bobo");
        rList.add("bobo2");
        System.out.println("redisson: " + rList.get(0));

        // map  set
    }
}
