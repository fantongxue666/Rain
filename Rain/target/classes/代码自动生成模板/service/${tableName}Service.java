package ${pPackage}.service;

import ${pPackage}.dao.${tableName?cap_first}Dao;
import ${pPackage}.model.${tableName?cap_first}Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ${tableName?cap_first}Service {
    @Autowired
    ${tableName?cap_first}Dao ${tableName}Dao;

    public List<Map> get${tableName?cap_first}List(){
        return ${tableName}Dao.get${tableName?cap_first}List();
    }

    public int add${tableName?cap_first}(${tableName?cap_first}Model ${tableName}Model){
        return ${tableName}Dao.add${tableName?cap_first}(${tableName}Model);
    }

    public ${tableName?cap_first}Model toEdit${tableName?cap_first}(${table[0].columnType} ${table[0].columnName}){
        return ${tableName}Dao.toEdit${tableName?cap_first}(${table[0].columnName});
    }

    public int update${tableName?cap_first}(${tableName?cap_first}Model ${tableName}Model){
        return ${tableName}Dao.update${tableName?cap_first}(${tableName}Model);
        }

    public int delete${tableName?cap_first}(${table[0].columnType} ${table[0].columnName}){
        return ${tableName}Dao.delete${tableName?cap_first}(${table[0].columnName});
    }
}
