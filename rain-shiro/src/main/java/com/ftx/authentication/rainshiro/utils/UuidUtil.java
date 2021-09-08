package com.ftx.authentication.rainshiro.utils;

import java.util.UUID;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName UuidUtil.java
 * @Description TODO
 * @createTime 2020年10月20日 10:53:00
 */
public class UuidUtil {
    public static String getUuid(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
