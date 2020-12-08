package com.ftx.exportexcel.rainexcel.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName ExcelMapping.java
 * @Description TODO
 * @createTime 2020年12月08日 19:35:00
 */
@ApiModel(value = "Excel映射实体类")
public class ExcelMapping {
    @ApiModelProperty("id（新增时系统自动生成）")
    private String id;
    @ApiModelProperty(value = "（表）列名")
    private String columnName;
    @ApiModelProperty(value = "（表）列数据格式")
    private String columnType;
    @ApiModelProperty(value = "对应Excel的列索引值")
    private Integer excelIndex;
    @ApiModelProperty(value = "表名")
    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public Integer getExcelIndex() {
        return excelIndex;
    }

    public void setExcelIndex(Integer excelIndex) {
        this.excelIndex = excelIndex;
    }
}
