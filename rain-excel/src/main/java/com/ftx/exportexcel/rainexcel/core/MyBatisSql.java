package com.ftx.exportexcel.rainexcel.core;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.poi.ss.usermodel.DateUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @createTime 2021年09月07日 09:57:00
 */
public class MyBatisSql {
    /**
     * 运行期获取MyBatis执行的SQL及参数
     *
     * @param id                Mapper xml 文件里的select Id
     * @param parameterMap      参数
     * @param sqlSessionFactory
     * @return
     */
    public static String getMyBatisSql(String id, Map<String, Object> parameterMap, SqlSessionFactory sqlSessionFactory) {
        Configuration configuration = sqlSessionFactory.getConfiguration();
        MappedStatement ms = configuration.getMappedStatement(id);
        BoundSql boundSql = ms.getBoundSql(parameterMap);
        //获取sql（此sql带有占位符）
        String sql = boundSql.getSql();
        //获取参数
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        Object parameterObject = boundSql.getParameterObject();
        //得到类型注册管理器
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        //如果参数不为空
        if (parameterMappings != null) {
            String parameter = "null";
            String propertyName;
            MetaObject newMetaObject = configuration.newMetaObject(parameterObject);
            for (ParameterMapping parameterMapping : parameterMappings) {
                if (parameterMapping.getMode() == ParameterMode.OUT) {
                    continue;
                }
                propertyName = parameterMapping.getProperty();
                // 注册管理器中是否注册了 parameterObject
                if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                    parameter = getParameterValue(parameterObject);
                    // MetaObject 是否有 propertyName 的getter方法
                } else if (newMetaObject.hasGetter(propertyName)) {
                    parameter = getParameterValue(newMetaObject.getValue(propertyName));
                } else if (boundSql.hasAdditionalParameter(propertyName)) {
                    parameter = getParameterValue(boundSql.getAdditionalParameter(propertyName));
                }
                sql = sql.replaceFirst("\\?", parameter);
            }
            return sql;
        }
        return null;
    }

    /**
     * 获取参数
     *
     * @param param Object类型参数
     * @return 转换之后的参数
     */
    private static String getParameterValue(Object param) {
        if (param == null) {
            return "null";
        }
        if (param instanceof Number) {
            return param.toString();
        }
        String value = null;
        if (param instanceof String) {
            value = "'" + param.toString() + "'";
            ;
        } else if (param instanceof Date) {
//            value = "'"+ DateUtil.dateToString_yyyy_MM_dd__HH_mm_ss((Date)param) +"'";;;
//            value = dateToString(param);
            value = param.toString();
        } else if (param instanceof Enum) {
            value = "'" + ((Enum<?>) param).name() + "'";
            ;
        } else {
            value = param.toString();
        }
        return value;
    }

    /**
     * 日期占位符替换
     */
    private static String dateToString(Object date) {

        return null;
    }
}
