package com.ftx.authentication.rainshiro;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;

@SpringBootApplication
@ServletComponentScan
@EnableSwagger2
@ComponentScan(basePackages = {"com.ftx.authentication.rainshiro.**","cn.zifangsky.license.**"})
public class RainShiroApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(RainShiroApplication.class);
        application.setBannerMode(Banner.Mode.CONSOLE);
        application.run(args);
//            SpringApplication.run(RainShiroApplication.class, args);


    }


}
