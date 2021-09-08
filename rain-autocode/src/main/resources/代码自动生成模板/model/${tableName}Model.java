package ${pPackage}.model;


public class ${tableName?cap_first}Model {  //?cap_first freemarker内置函数 首字母大写

<#list table as abc>
private ${abc.columnType} ${abc.columnName};
</#list>



<#list table as abc>
public ${abc.columnType} get${abc.columnName?cap_first}() {
        return this.${abc.columnName};
        }

public void set${abc.columnName?cap_first}(${abc.columnType} ${abc.columnName}) {
        this.${abc.columnName} = ${abc.columnName};
        }
</#list>


        }
