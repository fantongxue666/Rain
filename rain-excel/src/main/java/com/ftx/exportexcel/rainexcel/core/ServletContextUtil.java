package com.ftx.exportexcel.rainexcel.core;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * @author FanJiangFeng
 * @createTime 2021年09月07日 09:03:00
 */
public class ServletContextUtil {


    /**
     * 从上下文中/spring容器中获取Bean对象
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> tClass){
        //从上下文中获取SqlSessionFactory
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        ServletContext servletContext = request.getServletContext();
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        T bean = context.getBean(tClass);
        return bean;
    }
}
