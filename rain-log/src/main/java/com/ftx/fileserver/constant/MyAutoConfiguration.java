package com.ftx.fileserver.constant;

import com.ftx.fileserver.config.LogAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName MyAutoConfiguration.java
 * @Description TODO
 * @createTime 2021年05月06日 11:20:00
 */
@Configuration
@ConditionalOnBean(StartLog.class) //动态插拔
@EnableConfigurationProperties(RainLogProperties.class)
public class MyAutoConfiguration {
    static {
        System.out.println("====================== 开启RainLog日志 ========================");
    }

    @Bean
    public AspectJExpressionPointcutAdvisor configurabledvisor() {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression("@annotation(com.ftx.fileserver.constant.RainLog)");
        advisor.setAdvice(new LogAdvice());
        return advisor;
    }

    @Autowired
    RainLogProperties rainLogProperties;

    @Bean
    @ConditionalOnMissingBean(RainLogBean.class)
    public RainLogBean getRainLogBean(){
        System.out.println("实例化了RainLogBean");
        RainLogBean rainLogBean=new RainLogBean();
        rainLogBean.setPointCut_property(rainLogProperties.getProperty());
        return rainLogBean;
    }



}
