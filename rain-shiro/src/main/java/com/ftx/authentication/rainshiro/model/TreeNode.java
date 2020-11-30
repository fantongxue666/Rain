package com.ftx.authentication.rainshiro.model;

import java.util.List;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName TreeNode.java
 * @Description TODO
 * @createTime 2020年11月28日 17:32:00
 */
public class TreeNode {
    private Integer id;
    private Integer parentid;
    private String name;
    private String url;
    private String component;
    private String icon;
    private String roles;
    private List<TreeNode> children;

    public Integer getParentid() {
        return parentid;
    }

    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }
}
