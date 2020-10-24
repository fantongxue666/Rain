package com.ftx.exportexcel.rainexcel.mapper;

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
}
