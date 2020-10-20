package com.ftx.authentication.rainshiro.model;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName AuthUser.java
 * @Description TODO
 * @createTime 2020年10月19日 17:19:00
 */
public class AuthUser {
    private String account;//账号
    private String pwd;//密码
    private String id;//id
    private String username;//昵称
    private String ip;//登录人IP地址
    private String token;//token

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
