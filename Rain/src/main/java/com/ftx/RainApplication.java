package com.ftx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = {"com.ftx.**","com.ftx.authentication.rainshiro.**","com.rain.platentity.**"})
public class RainApplication{
    public static void main(String[] args) {
        SpringApplication.run(RainApplication.class, args);
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" +
                ">>>>>> bootstrap美化后的接口文档地址：http://localhost:8080/doc.html >>>>>>>" +
                ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

}
