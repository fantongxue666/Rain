package ${pPackage}.dao;
import ${pPackage}.model.${tableName?cap_first}Model;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Mapper
@Repository
public interface ${tableName?cap_first}Dao {

    /**
     * 查询
     */
    @Select(value = "select * from ${tableName}")
    List<Map> get${tableName?cap_first}List();

    /**
     * 添加
     */
    @Insert(value = "insert into ${tableName}(<#list table as abc>${abc.columnName}<#if abc_has_next>,</#if></#list>)values(<#list table as abc><#noparse>#{</#noparse>${abc.columnName}}<#if abc_has_next>,</#if></#list>)")
    int add${tableName?cap_first}(${tableName?cap_first}Model ${tableName}Model);

    /**
     * 待修改
     */
    @Select(value = "select * from ${tableName} where ${table[0].columnName} = <#noparse>#{</#noparse>${table[0].columnName}<#noparse>}</#noparse>")
    ${tableName?cap_first}Model toEdit${tableName?cap_first}(${table[0].columnType} ${table[0].columnName});

    /**
     * 修改
     */
    @Update(value = "update ${tableName} set <#list table as abc>${abc.columnName}=<#noparse>#{</#noparse>${abc.columnName}<#noparse>}</#noparse><#if abc_has_next>,</#if></#list> where ${table[0].columnName} = <#noparse>#{</#noparse>${table[0].columnName}<#noparse>}</#noparse>")
    int update${tableName?cap_first}(${tableName?cap_first}Model ${tableName}Model);

    /**
     * 删除
     */
    @Delete(value = "delete from ${tableName} where ${table[0].columnName} = <#noparse>#{</#noparse>${table[0].columnName}<#noparse>}</#noparse>")
    int delete${tableName?cap_first}(${table[0].columnType} ${table[0].columnName});

}
