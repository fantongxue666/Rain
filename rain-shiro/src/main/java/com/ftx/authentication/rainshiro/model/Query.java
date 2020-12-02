package com.ftx.authentication.rainshiro.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName Query.java
 * @Description TODO
 * @createTime 2020年12月02日 17:29:00
 */
@ApiModel(value = "Query分页实体")
public class Query {
    @ApiModelProperty(value = "当前页")
    private Integer pageNo;
    @ApiModelProperty(value = "页大小")
    private Integer pageSize;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
