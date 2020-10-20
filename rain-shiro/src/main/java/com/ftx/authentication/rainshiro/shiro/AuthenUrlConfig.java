package com.ftx.authentication.rainshiro.shiro;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName AuthenUrlConfig.java
 * @Description TODO shiro自定义配置
 * @createTime 2020年10月19日 18:17:00
 */
@Configuration
@Component
@ConfigurationProperties(prefix = "rain.shiro")
public class AuthenUrlConfig {
    private String loginUrl="/login";//放开登录路径
    private String logoutUrl="/logout";//放开登出路径
    private boolean isCheckToken=true;//是否校验token
    private String tokenName="RAIN-TOKEN";//请求头token名字
    private Long tokenLiveTime=1800L;//token存活时间

    public Long getTokenLiveTime() {
        return tokenLiveTime;
    }

    public void setTokenLiveTime(Long tokenLiveTime) {
        this.tokenLiveTime = tokenLiveTime;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public boolean isCheckToken() {
        return isCheckToken;
    }

    public void setCheckToken(boolean checkToken) {
        isCheckToken = checkToken;
    }

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
