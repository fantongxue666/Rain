package com.ftx.authentication.rainshiro.shiro;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName AuthenUrlConfig.java
 * @Description TODO 设置shiro过滤器放开的请求路径
 * @createTime 2020年10月19日 18:17:00
 */
@Configuration
@Component
@ConfigurationProperties(prefix = "rain.shiro")
public class AuthenUrlConfig {
    private String loginUrl;//登录路径
    private String logoutUrl;//登出路径

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }
}
