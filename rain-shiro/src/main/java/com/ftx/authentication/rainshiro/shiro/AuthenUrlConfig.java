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
    private boolean isCheckToken;//是否校验token
    private String tokenName;//请求头token名字
    private Long tokenLiveTime;//token存活时间
    private String[] freeUrls;//白名单

    public String[] getFreeUrls() {
        return freeUrls;
    }

    public void setFreeUrls(String[] freeUrls) {
        this.freeUrls = freeUrls;
    }

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


}
