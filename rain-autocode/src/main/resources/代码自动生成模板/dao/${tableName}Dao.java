package ${pPackage}.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
@Mapper
public interface ${tableName?cap_first}Dao {

    /**
     * 查询
     */
    @Select(value = "select * from ${tableName}")
    List<Map> get${tableName?cap_first}List();

    /**
     * 添加
     */
    int add${tableName?cap_first}(Map map);

    /**
     * 待修改
     */
    Map toEdit${tableName?cap_first}(${table[0].columnType} ${table[0].columnName});

    /**
     * 修改
     */
    int update${tableName?cap_first}(Map map);

    /**
     * 删除
     */
    int delete${tableName?cap_first}(${table[0].columnType} ${table[0].columnName});

}
