package com.ftx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ftx.**","com.ftx.authentication.rainshiro.**"})
public class RainApplication{

    public static void main(String[] args) {
        SpringApplication.run(RainApplication.class, args);
    }

}
