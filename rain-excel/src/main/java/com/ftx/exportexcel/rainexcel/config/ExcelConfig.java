package com.ftx.exportexcel.rainexcel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName ExcelConfig.java
 * @Description TODO
 * @createTime 2020年10月24日 11:04:00
 */
@Configuration
@Component
@ConfigurationProperties(prefix = "rain.excel")
public class ExcelConfig {
    private String resource;//存储excel模板和excel查询xml文件的文件夹位置

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
