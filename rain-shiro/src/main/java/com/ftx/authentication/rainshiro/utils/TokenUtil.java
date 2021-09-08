package com.ftx.authentication.rainshiro.utils;
import com.ftx.authentication.rainshiro.shiro.AuthenUrlConfig;
import jdk.nashorn.internal.parser.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
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

    /**
     * getTokenBefore方法和getTokenTime方法：去spring上下文获取配置文件的自定义配置
     * @param request
     * @return
     */
    public String getTokenBefore(HttpServletRequest request){
        ServletContext servletContext = request.getSession().getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        String token_name="";
        if(webApplicationContext!=null){
            Environment environment = webApplicationContext.getBean(Environment.class);
             token_name = environment.getProperty("rain.shiro.token-name");
        }
        return token_name;
    }

    public Long getTokenTime(HttpServletRequest request){
        ServletContext servletContext = request.getSession().getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        String token_time="";
        long parseLong;
        if(webApplicationContext!=null){
            Environment environment = webApplicationContext.getBean(Environment.class);
            token_time = environment.getProperty("rain.shiro.tokenLiveTime");
             parseLong = Long.parseLong(token_time);
            return parseLong;
        }else{
            return null;
        }
    }

    //设置token
    public String setToken(HttpServletRequest request){
       String token = UuidUtil.getUuid()+UuidUtil.getUuid();
       //键：RAIN-TOKEN/8asdfg096as90d-fg69asd-g6as-dg6asd
        String key=this.getTokenBefore(request)+"/"+token;
        redisUtil.set(key,token,this.getTokenTime(request), TimeUnit.MINUTES);
        logger.info("设置token有效时间------------"+this.getTokenTime(request)/60+"分钟");
        return token;
    }

    //得到token
    public String getToken(HttpServletRequest request){
        String token=request.getHeader(this.getTokenBefore(request));
        if (!StringUtils.isEmpty(token) && token.equals("undefined")) {
            token = null;
        }
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter(this.getTokenBefore(request));
        }

        return token;
    }

    //校验token
    public boolean validateToken(String token,HttpServletRequest request){
    String key=this.getTokenBefore(request)+"/"+token;
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
