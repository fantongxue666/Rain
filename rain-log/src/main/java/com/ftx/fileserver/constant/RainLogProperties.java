package com.ftx.fileserver.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName RainLogProperties.java
 * @Description TODO
 * @createTime 2021年05月06日 11:17:00
 */
@ConfigurationProperties(prefix = "pointcut.property")
public class RainLogProperties {
    private static final String property_default="@annotation(com.ftx.fileserver.constant.RainLog)";
    private String property = property_default;

    public static String getProperty_default() {
        return property_default;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
