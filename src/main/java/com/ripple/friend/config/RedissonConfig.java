package com.ripple.friend.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description Redisson 配置
Redisson 是一个 java 操作 Redis 的客户端，提供了大量的分布式数据集来简化对 Redis 的操作和使用，
可以让开发者想使用本地集合一样使用 Redis，完全感知不到 Redis 的存在
 */

@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {

    private String host;

    private String port;

    private String password;

    @Bean
    public RedissonClient redissonClient() {
        // 1. 创建配置
        Config config = new Config();
        String redisAddress = String.format("redis://%s:%s", host, port);
        // 分布式锁属于一个单独的服务，所以选择另外一个数据库
        config.useSingleServer().setAddress(redisAddress).setDatabase(3);
        // 注意需要配置 redis 的密码，否则无法连接
        config.useSingleServer().setPassword(password);
        // 2. 创建实例
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
