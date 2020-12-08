package com.ftx.exportexcel.rainexcel.controller;

import com.ftx.exportexcel.rainexcel.config.JsonObject;
import com.ftx.exportexcel.rainexcel.mapper.ExportMapper;
import com.ftx.exportexcel.rainexcel.model.ExcelMapping;
import com.ftx.exportexcel.rainexcel.model.ExcelMemo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.UUID;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName ImportController.java
 * @Description TODO
 * @createTime 2020年12月08日 19:30:00
 */
@RestController
@Api(tags = "导入Excel配置")
public class ImportController {

    @Autowired
    ExportMapper exportMapper;

    @PostMapping("/excelMapping/addSetting")
    @ApiOperation(value = "新增Excel配置场景")
    public JsonObject addSetting(ExcelMemo excelMemo){
        excelMemo.setId(UUID.randomUUID().toString().replaceAll("-",""));
        int i = exportMapper.insertExcelMemo(excelMemo);
        return i>0?JsonObject.Success():JsonObject.Error();
    }

    @PostMapping("/excelMapping/addMapping")
    @ApiOperation(value = "批量设置列映射")
    public void addMapping(List<ExcelMapping> excelMapping){
    for(ExcelMapping mapping:excelMapping){
        mapping.setId(UUID.randomUUID().toString().replaceAll("-",""));
    }


    }
}
