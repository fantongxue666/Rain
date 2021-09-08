package com.ftx.constant;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName APPEnums.java
 * @Description TODO
 * @createTime 2020年10月20日 14:22:00
 */
public enum APPEnums {
    OK(200, "操作成功！"),
    ERROR_TIP(101, "错误提示"),
    ERROR(500, "内部服务器错误"),
    PARAM_ERROR_TIP(102, "请求错误错误"),
    TOKE_INVALIDATE(501, "登录token无效，请重新登录"),
    LOGIN_NAME_ERROR(502, "登录用户名错误"),
    LOGIN_PASSWORD_ERROR(503, "登录密码错误"),
    ACCOUNT_LOCKED_ERROR(504, "登录账号被锁定，请联系管理员处理"),
    PAGE_NOT_FOUND(404, "页面不存在"),
    ACCESS_NO(403, "您没有访问权限"),
    UNAUTHORIZED_ERROR(401, "请先登录");

    private Integer code;
    private String message;

    private APPEnums(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

}
