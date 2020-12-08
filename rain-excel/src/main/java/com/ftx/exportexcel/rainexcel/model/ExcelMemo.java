package com.ftx.exportexcel.rainexcel.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName ExcelMemo.java
 * @Description TODO
 * @createTime 2020年12月08日 20:03:00
 */
@ApiModel(value = "Excel配置实体类")
public class ExcelMemo {
    @ApiModelProperty(value = "id（新增时系统自动生成）")
    private String id;
    @ApiModelProperty(value = "表名")
    private String tableName;
    @ApiModelProperty(value = "数据开始的行索引")
    private Integer beginRowIndex;
    @ApiModelProperty(value = "数据结束的行索引")
    private Integer endRowIndex;
    @ApiModelProperty(value = "数据开始的列索引")
    private Integer beginColIndex;
    @ApiModelProperty(value = "数据结束的列索引")
    private Integer endColIndex;
    @ApiModelProperty(value = "导入excel的场景")
    private String memo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getBeginRowIndex() {
        return beginRowIndex;
    }

    public void setBeginRowIndex(Integer beginRowIndex) {
        this.beginRowIndex = beginRowIndex;
    }

    public Integer getEndRowIndex() {
        return endRowIndex;
    }

    public void setEndRowIndex(Integer endRowIndex) {
        this.endRowIndex = endRowIndex;
    }

    public Integer getBeginColIndex() {
        return beginColIndex;
    }

    public void setBeginColIndex(Integer beginColIndex) {
        this.beginColIndex = beginColIndex;
    }

    public Integer getEndColIndex() {
        return endColIndex;
    }

    public void setEndColIndex(Integer endColIndex) {
        this.endColIndex = endColIndex;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

}
