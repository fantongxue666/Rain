package com.ftx.authentication.rainshiro.redis;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName GlobalExceptionHandler.java
 * @Description TODO
 * @createTime 2020年10月30日 13:32:00
 */

import com.ftx.authentication.rainshiro.constant.APPEnums;
import com.ftx.authentication.rainshiro.constant.JsonObject;
import com.ftx.authentication.rainshiro.utils.TokenException;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于捕获全局token异常
 */

@RestControllerAdvice//控制器切面
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenException.class)
    public JsonObject<Object> runtime(RuntimeException runtimeException){
        return new JsonObject<>("token失效，请重新登录",APPEnums.TOKE_INVALIDATE);
    }
}
