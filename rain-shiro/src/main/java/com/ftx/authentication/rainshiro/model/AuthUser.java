package com.ftx.authentication.rainshiro.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName AuthUser.java
 * @Description TODO
 * @createTime 2020年10月19日 17:19:00
 */
@ApiModel("用户类")
public class AuthUser extends Query{
    @ApiModelProperty(name = "account",value = "账号")
    private String account;//账号
    @ApiModelProperty(name = "pwd",value = "密码")
    private String pwd;//密码
    @ApiModelProperty(name = "id",value = "ID")
    private String id;//id
    @ApiModelProperty(name = "username",value = "昵称")
    private String username;//昵称
    @ApiModelProperty(name = "ip",value = "登录人ip地址")
    private String ip;//登录人IP地址
    @ApiModelProperty(name = "token",value = "令牌")
    private String token;//token
    @ApiModelProperty(name = "powers",value = "权限列表")
    private List<String> powerList;
    @ApiModelProperty(name = "rolename",value = "角色")
    private String rolename;

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public List<String> getPowerList() {
        return powerList;
    }

    public void setPowerList(List<String> powerList) {
        this.powerList = powerList;
    }

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
