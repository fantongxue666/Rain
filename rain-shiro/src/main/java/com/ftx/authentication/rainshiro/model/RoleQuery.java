package com.ftx.authentication.rainshiro.model;

import java.util.Date;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName RoleQuery.java
 * @Description TODO
 * @createTime 2020年12月02日 17:30:00
 */
public class RoleQuery extends Query {
    private String id;
    private String name;
    private String status;
    private Date createtime;

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
