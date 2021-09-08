package com.ftx.fileserver.constant;

import java.lang.annotation.*;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName RainLog.java
 * @Description TODO
 * @createTime 2021年05月06日 10:06:00
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RainLog {

    //操作描述
    String remark() default "";
}
