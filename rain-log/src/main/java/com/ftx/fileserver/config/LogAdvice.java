package com.ftx.fileserver.config;

import com.ftx.fileserver.constant.RainLog;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName LogAdvice.java
 * @Description TODO
 * @createTime 2021年05月06日 10:16:00
 */
public class LogAdvice implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        if(method.isAnnotationPresent(RainLog.class)){
            RainLog annotation = method.getAnnotation(RainLog.class);
            String remark = annotation.remark();//日志备注
            System.out.println("日志备注："+remark);
            Parameter[] parameters = method.getParameters();
            for(Parameter parameter:parameters){
                System.out.println("方法的参数："+parameter.getName());
            }
        }
        Object result = methodInvocation.proceed();
        return result;
    }
}
