package com.ripple.friend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


//@EnableRedisHttpSession
@SpringBootApplication
@MapperScan("com.ripple.friend.mapper")
@EnableScheduling  // 开启spring 定时任务的支持
public class FriendMatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(FriendMatchApplication.class, args);
    }

}
