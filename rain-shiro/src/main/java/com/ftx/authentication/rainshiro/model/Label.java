package com.ftx.authentication.rainshiro.model;

import java.util.List;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName Label.java
 * @Description TODO
 * @createTime 2020年12月05日 10:04:00
 */
public class Label {
    private Integer id;
    private String label;
    private List<Label> children;
    private List<Integer> ids;

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Label> getChildren() {
        return children;
    }

    public void setChildren(List<Label> children) {
        this.children = children;
    }
}
