package com.ftx.authentication.rainshiro.shiro;

import com.alibaba.fastjson.JSON;
import com.ftx.authentication.rainshiro.utils.RedisUtil;
import com.ftx.authentication.rainshiro.utils.TokenException;
import com.ftx.authentication.rainshiro.utils.TokenUtil;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName ShiroLoginFilter.java
 * @Description TODO
 * @createTime 2020年10月20日 09:48:00
 */
@Configuration
public class ShiroLoginFilter extends FormAuthenticationFilter {
    Logger logger= LoggerFactory.getLogger(ShiroLoginFilter.class);
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    AuthenUrlConfig authenUrlConfig;
    //是否校验token
    private boolean isCheckToken;
    //token名字
    private String tokenName;
    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
//        logger.info("所有的请求经过过滤器都会来到此onPreHandle方法");
        HttpServletRequest req=(HttpServletRequest)request;
        //在filter中 依赖注入不生效了，因为加载顺序 listener>filter>servlet  因此需要用上下文对象来获取
        //需要获取RedisTemplate和配置文件的一个属性配置（是否校验token）
        ServletContext servletContext = req.getSession().getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        if(webApplicationContext!=null){
            redisTemplate=(RedisTemplate)webApplicationContext.getBean("redisTemplate");
            Environment environment = webApplicationContext.getBean(Environment.class);
            String check = environment.getProperty("rain.shiro.check-token");
            String token_name = environment.getProperty("rain.shiro.token-name");
            tokenName=token_name;
            if("true".equals(check)){
                isCheckToken=true;
            }else if("false".equals(check)){
                isCheckToken=false;
            }else{
                logger.error("配置文件属性----是否校验token----获取失败");
            }

        }
        return super.onPreHandle(request, response, mappedValue);
    }

    //判断是否登录
    //在登录的情况下会走此方法，此方法返回true直接访问控制器
    //如果isAccessAllowed方法返回True，则不会再调用onAccessDenied方法
    // 如果isAccessAllowed方法返回Flase,则会继续调用onAccessDenied方法
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)  {
        HttpServletRequest req=(HttpServletRequest)request;
        String requestURI = req.getRequestURI();
        //放开接口文档请求地址
        if(requestURI.contains("swagger")||requestURI.contains("v2")||"/".equals(requestURI)||"/csrf".equals(requestURI)){
            return true;
        }
        if(requestURI.contains(".html")||requestURI.contains(".css")||requestURI.contains(".js")||requestURI.contains(".ico")){
            return true;
        }
        String token = ShiroBeansUtil.getTokenUtil().getToken(req);
        if(StringUtils.isEmpty(token)){
            if(!isCheckToken){
                logger.info("开发环境已经开启，不校验token有效，无须登录即可访问接口");
                return true;
            }else{
                logger.error("获取token失败");
                throw new TokenException("token失效，请重新登录");
            }
        }else{
            //校验token是否有效
            boolean isOk = this.validateToken(token);
            if(isOk){
                logger.info("token有效，并更新延长token在redis中的存活时间【30分钟】");
                //延长半小时
                redisTemplate.expire(this.tokenName+"/"+token,1800L, TimeUnit.MINUTES);
                return true;
            }else if(!isCheckToken){
                logger.info("开发环境已经开启，不校验token有效，无须登录即可访问接口");
                return true;
            }else{
                logger.error("获取token失败");
                throw new TokenException("token失效，请重新登录");
            }
        }
    }

    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        logger.error("用户认证失败");
//        this.printJsonMsg((HttpServletResponse)response);
        throw new TokenException("token失效，请重新登录");
    }

    private void printJsonMsg(HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        String errorMsg = "用户认证失败";
        response.getWriter().print(errorMsg);
    }

    //是否是拒绝登录
    //没有登录的情况下会走此方法
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
//        logger.info("拒绝登录--没有登录会进入此方法");
        HttpServletResponse httpResp = WebUtils.toHttp(response);
        HttpServletRequest httpReq = WebUtils.toHttp(request);
        httpResp.setContentType("application/json; charset=utf-8");
        String origin = httpReq.getHeader("Origin");
        httpResp.addHeader("Access-Control-Allow-Origin","*");
        httpResp.addHeader("Access-Control-Allow-Headers", "*");
        httpResp.addHeader("Access-Control-Allow-Methods", "*");
        httpResp.addHeader("Access-Control-Allow-Credentials", "true");
        this.redirectToLogin(request, response);
        return false;
    }

    //校验token
    public boolean validateToken(String token){
        String key=this.tokenName+"/"+token;
        Object old_token_object = this.get(key);
        if(old_token_object!=null){
            String old_token = old_token_object.toString();
            if(token.equals(old_token)){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }

    }

    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }
}
