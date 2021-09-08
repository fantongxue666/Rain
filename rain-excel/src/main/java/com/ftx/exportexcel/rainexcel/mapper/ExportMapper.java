package com.ftx.exportexcel.rainexcel.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ExportMapper {

    @Select(value = "${_parameter}")
    List<Map> getData(String sql);

}
