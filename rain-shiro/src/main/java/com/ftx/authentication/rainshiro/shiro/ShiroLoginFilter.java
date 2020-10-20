package com.ftx.authentication.rainshiro.shiro;

import com.alibaba.fastjson.JSON;
import com.ftx.authentication.rainshiro.utils.RedisUtil;
import com.ftx.authentication.rainshiro.utils.TokenUtil;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    @Override
    public boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        logger.info("所有的请求经过过滤器都会来到此onPreHandle方法");
        return super.onPreHandle(request, response, mappedValue);
    }

    //判断是否登录
    //在登录的情况下会走此方法，此方法返回true直接访问控制器
    //如果isAccessAllowed方法返回True，则不会再调用onAccessDenied方法
    // 如果isAccessAllowed方法返回Flase,则会继续调用onAccessDenied方法
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest req=(HttpServletRequest)request;
        String token = ShiroBeansUtil.getTokenUtil().getToken(req);
        if(StringUtils.isEmpty(token)){
            if(!ShiroBeansUtil.getAuthenUrlConfig().isCheckToken()){
                logger.info("开发环境已经开启，不校验token有效");
                return true;
            }else{
                logger.error("获取token失败");
                return false;
            }
        }else{
            //校验token是否有效
            boolean isOk = ShiroBeansUtil.getTokenUtil().validateToken(token);
            if(isOk){
                logger.info("token有效，并更新延长token在redis中的存活时间【30分钟】");
                //延长半小时
                ShiroBeansUtil.getRedisTemplate().expire(ShiroBeansUtil.getAuthenUrlConfig().getTokenName()+"/"+token,ShiroBeansUtil.getAuthenUrlConfig().getTokenLiveTime(), TimeUnit.MINUTES);
                return true;
            }else if(!ShiroBeansUtil.getAuthenUrlConfig().isCheckToken()){
                logger.info("开发环境已经开启，不校验token有效");
                return true;
            }else{
                logger.error("获取token失败");
                return false;
            }
        }
    }

    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        logger.error("用户认证失败");
        this.printJsonMsg((HttpServletResponse)response);
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
        logger.info("拒绝登录--没有登录会进入此方法");
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
}
