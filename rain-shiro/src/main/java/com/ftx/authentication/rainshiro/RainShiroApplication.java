package com.ftx.authentication.rainshiro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class RainShiroApplication {

    public static void main(String[] args) {
        SpringApplication.run(RainShiroApplication.class, args);
    }


}
