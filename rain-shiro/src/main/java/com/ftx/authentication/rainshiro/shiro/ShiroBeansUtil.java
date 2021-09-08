package com.ftx.authentication.rainshiro.shiro;

import com.ftx.authentication.rainshiro.utils.RedisUtil;
import com.ftx.authentication.rainshiro.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName ShiroBeansUtil.java
 * @Description TODO
 * @createTime 2020年10月20日 15:45:00
 */
@Configuration
public class ShiroBeansUtil {
    @Autowired
    public static RedisTemplate getRedisTemplate(){
        return new RedisTemplate();
    }
    @Autowired
    public static TokenUtil getTokenUtil(){
        return new TokenUtil();
    }
    @Autowired
    public static RedisUtil getRedisUtil(){
        return new RedisUtil();
    }
    @Autowired
    public static AuthenUrlConfig getAuthenUrlConfig(){
        return new AuthenUrlConfig();
    }
}
