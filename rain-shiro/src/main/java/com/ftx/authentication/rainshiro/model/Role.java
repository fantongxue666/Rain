package com.ftx.authentication.rainshiro.model;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName Role.java
 * @Description TODO
 * @createTime 2020年12月05日 15:58:00
 */
public class Role {
    private String id;
    private String roleid;
    private Integer powerid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public Integer getPowerid() {
        return powerid;
    }

    public void setPowerid(Integer powerid) {
        this.powerid = powerid;
    }
}
