package com.ftx.autocode.generation.config;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName Column.java
 * @Description TODO
 * @createTime 2020年04月23日 14:55:00
 */
public class Column {

    private String columnName;//列名称
    private String columnName2;//处理后的列名称
    private String columnType;//列类型
    private String columnDbType;//列在数据库中的类型

    //本工程暂不处理备注和主键
    private String columnComment;//列备注id
    private String columnKey;//是否是主键

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName2() {
        return columnName2;
    }

    public void setColumnName2(String columnName2) {
        this.columnName2 = columnName2;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnDbType() {
        return columnDbType;
    }

    public void setColumnDbType(String columnDbType) {
        this.columnDbType = columnDbType;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    @Override
    public String toString() {
        return "Column{" +
                "columnName='" + columnName + '\'' +
                ", columnName2='" + columnName2 + '\'' +
                ", columnType='" + columnType + '\'' +
                ", columnDbType='" + columnDbType + '\'' +
                ", columnComment='" + columnComment + '\'' +
                ", columnKey='" + columnKey + '\'' +
                '}';
    }
}
