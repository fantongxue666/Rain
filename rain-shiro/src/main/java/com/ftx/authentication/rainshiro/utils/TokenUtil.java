package com.ftx.authentication.rainshiro.utils;
import com.ftx.authentication.rainshiro.shiro.AuthenUrlConfig;
import jdk.nashorn.internal.parser.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName TokenUtil.java
 * @Description TODO
 * @createTime 2020年10月20日 10:45:00
 */
@Component
public class TokenUtil {
    Logger logger= LoggerFactory.getLogger(TokenUtil.class);
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    AuthenUrlConfig authenUrlConfig;

    public TokenUtil(){
        if(this.authenUrlConfig==null){
            authenUrlConfig=new AuthenUrlConfig();
        }
        if(this.redisUtil==null){
            this.redisUtil=new RedisUtil();
        }
    }

    //设置token
    public String setToken(){
       String token = UuidUtil.getUuid()+UuidUtil.getUuid();
       //键：RAIN-TOKEN/8asdfg096as90d-fg69asd-g6as-dg6asd
        String key=authenUrlConfig.getTokenName()+"/"+token;
        redisUtil.set(key,token,authenUrlConfig.getTokenLiveTime(), TimeUnit.MINUTES);
        logger.info("设置token有效时间------------"+authenUrlConfig.getTokenLiveTime()/60+"分钟");
        return token;
    }

    //得到token
    public String getToken(HttpServletRequest request){
        String token=request.getHeader(authenUrlConfig.getTokenName());
        if (!StringUtils.isEmpty(token) && token.equals("undefined")) {
            token = null;
        }
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter(authenUrlConfig.getTokenName());
        }

        return token;
    }

    //校验token
    public boolean validateToken(String token){
    String key=authenUrlConfig.getTokenName()+"/"+token;
        String old_token = redisUtil.get(key).toString();
        if(old_token!=null){
            if(token.equals(old_token)){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }

    }

}
