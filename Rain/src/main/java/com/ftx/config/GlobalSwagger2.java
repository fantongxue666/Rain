package com.ftx.config;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName GlobalSwagger2.java
 * @Description TODO
 * @createTime 2020年10月28日 18:12:00
 */
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class GlobalSwagger2 {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //生成api文档扫描路径
                .apis(RequestHandlerSelectors.basePackage("cn.ftx.**"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("Rain平台")
                //作者
                .contact(new Contact("樊同学", "https://github.com/fantongxue666/Rain", "fantongxue666@163.com"))
                //版本号
                .version("1.0")
                //描述
                .description("Rain平台，是由多个模块集成的且成熟的系统平台，宗旨是为了开发者打造一套易上手，易配置，即插即用的后台管理系统架构，开发中")
                .build();
    }
}
