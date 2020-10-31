package ${pPackage}.controller;

import ${pPackage}.constant.JsonObject;
import ${pPackage}.model.${tableName?cap_first}Model;
import ${pPackage}.service.${tableName?cap_first}Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/${tableName}ctrl")
@Api(tags = "${description}")
public class ${tableName?cap_first}Controller {
    @Autowired
    ${tableName?cap_first}Service ${tableName}Service;

    @ApiOperation(value = "列表查询")
    @PostMapping("/findList")
    public JsonObject<List<Map>> get${tableName?cap_first}List(){
        List<Map> ${tableName}List = ${tableName}Service.get${tableName?cap_first}List();
        return new JsonObject<>(${tableName}List);
    }

    @ApiOperation(value = "新增")
    @PostMapping("/add")
    public JsonObject<Object> add${tableName?cap_first}(${tableName?cap_first}Model ${tableName}Model){
        int i = ${tableName}Service.add${tableName?cap_first}(${tableName}Model);
        return i>0?JsonObject.Success():JsonObject.Error();
    }

    @ApiOperation(value = "加载")
    @PostMapping("/getById")
    public JsonObject<Object> toEdit${tableName?cap_first}(String id){
        ${tableName?cap_first}Model ${tableName}Model = ${tableName}Service.toEdit${tableName?cap_first}(id);
        return new JsonObject<>(${tableName}Model);
    }

    @ApiOperation(value = "更新")
    @PostMapping("/update")
    public JsonObject<Object> update${tableName?cap_first}(${tableName?cap_first}Model ${tableName}Model){
        int i = ${tableName}Service.update${tableName?cap_first}(${tableName}Model);
        return i>0?JsonObject.Success():JsonObject.Error();
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delById")
    public JsonObject<Object> delete${tableName?cap_first}(String id){
        int i = ${tableName}Service.delete${tableName?cap_first}(id);
        return i>0?JsonObject.Success():JsonObject.Error();
    }




}
