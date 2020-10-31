package com.ftx.autocode.generation.constant;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName JsonObject.java
 * @Description TODO
 * @createTime 2020年10月20日 14:19:00
 */
//数据返回封装类
public class JsonObject<T> {
    private T data;//返回的数据
    private Integer code;//状态编码
    private String message;//提示信息
    private Long timeStamp;//当前数据返回的时间戳

    public static JsonObject Success(){
        return new JsonObject("操作成功",APPEnums.OK);
    }
    public static JsonObject Error(){
        return new JsonObject("操作失败",APPEnums.ERROR);
    }

    public JsonObject(T data) {
        this(data, APPEnums.OK);
    }

    public JsonObject(T data, Integer code) {
        this(data, code, (String)null);
    }

    public JsonObject(T data, APPEnums appEnums) {
        this(data, appEnums.getCode(), appEnums.getMessage());
    }

    public JsonObject(T data, Integer code, String message) {
        this.data = data;
        this.code = code;
        this.message = message;
        this.timeStamp = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
