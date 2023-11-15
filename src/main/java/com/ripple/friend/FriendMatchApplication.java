package com.ripple.friend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ripple.friend.mapper")
public class FriendMatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(FriendMatchApplication.class, args);
    }

}
