package com.rain.platentity.autoconfiguration;

import com.rain.platentity.inteceptor.ParamsInteceptor;
import com.rain.platentity.inteceptor.ResultInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName MyAutoConfiguration.java
 * @Description TODO
 * @createTime 2021年06月03日 13:09:00
 */
@Configuration
public class MyAutoConfiguration {

    @Bean
    public ParamsInteceptor getParamsInteceptor(){
        return new ParamsInteceptor();
    }

    @Bean
    public ResultInterceptor getResultInterceptor(){
        return new ResultInterceptor();
    }
}
