package com.ftx.exportexcel.rainexcel.mapper;

import com.ftx.exportexcel.rainexcel.model.ExcelMapping;
import com.ftx.exportexcel.rainexcel.model.ExcelMemo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName ExportMapper.java
 * @Description TODO
 * @createTime 2020年10月24日 14:38:00
 */
@Mapper
@Repository
public interface ExportMapper {

    @Select(value = "${_parameter}")
    List<Map> getData(String sql);

    @Insert(value = "insert into tb_excel_memo values(#{id},#{tableName},#{beginRowIndex},#{endRowIndex},#{beginColIndex},#{endColIndex},#{memo})")
    int insertExcelMemo(ExcelMemo excelMemo);

    @Insert({"<script>",
            "insert into tb_excel_mapping(id,columnName,columnType,excelIndex) values",
            "<foreach collection='list',item='item',index='index',separator=','>",
            "(",
            "#{item.id},#{item.columnName},#{item.columnType},#{item.excelIndex}",
            ")",
            "</foreach>",
            "</script>"})
    int insertExcelMapping(List<ExcelMapping> list);
}
