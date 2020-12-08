package com.ftx.constant;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName GlobalSwagger2.java
 * @Description TODO
 * @createTime 2020年10月28日 18:12:00
 */
import io.swagger.annotations.ApiOperation;
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
public class GlobalSwagger {
    @Bean
    public Docket createRestApi() {
        System.out.println("======  SWAGGER CONFIG  ======");
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Fast 疾速开发  RESTful APIs")
                .description("快速上手,快速开发,快速交接")
                .contact(new Contact("geYang", "https://my.oschina.net/u/3681868/home", "572119197@qq.com"))
                .version("1.0.0")
                .build();
    }
}
