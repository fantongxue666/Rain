package com.ftx.autocode.generation.config;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName JavaKind.java
 * @Description TODO  sql类型和java类型的替换规则
 * @createTime 2020年10月31日 10:56:00
 */
public class JavaKind {
    public static final String VARCHAR="String";
    public static final String BIGINT="Long";
    public static final String NUMBER="Long";
    public static final String INT="Integer";
    public static final String DATE="java.util.Date";
    public static final String DATETIME="java.util.Date";
    public static final String DOUBLE="Double";
    public static final String TEXT="String";
    public static final String VARCHAR2="String";
    public static final String NVARCHAR2="String";
    public static final String CHAR="String";

}
