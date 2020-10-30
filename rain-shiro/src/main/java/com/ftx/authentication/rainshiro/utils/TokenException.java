package com.ftx.authentication.rainshiro.utils;

import org.springframework.context.annotation.Configuration;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName TokenException.java
 * @Description TODO 自定义token校验失败异常
 * @createTime 2020年10月30日 13:31:00
 */
@Configuration
public class TokenException extends RuntimeException {

    private static final long serialVersionUID = 4692834141778661750L;
    /**
     * 无参数自定义业务异常构造方法.
     */
    public TokenException() {
    }

    /**
     *
     * @param message
     *            异常消息.
     */
    public TokenException(String message) {
        super(message);
    }

    /**
     *
     * @param cause
     *            根异常.
     */
    public TokenException(Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param message
     *            异常消息.
     * @param cause
     *            根异常.
     */
    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     *
     * @param message
     *            异常消息.
     * @param cause
     *            根异常.
     */
    public TokenException(String message, String code) {
        super(message);
    }
}
