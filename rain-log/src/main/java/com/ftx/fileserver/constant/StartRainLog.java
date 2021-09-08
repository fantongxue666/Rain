package com.ftx.fileserver.constant;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName StartRainLog.java
 * @Description TODO
 * @createTime 2021年05月06日 11:22:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(StartLog.class)
public @interface StartRainLog {
}
