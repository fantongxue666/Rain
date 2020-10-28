package com.ftx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>" +
                ">>>>>> 接口文档地址：http://localhost:8080/swagger-ui.html >>>>>>>" +
                ">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

}
