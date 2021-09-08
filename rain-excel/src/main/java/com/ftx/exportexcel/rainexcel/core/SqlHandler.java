package com.ftx.exportexcel.rainexcel.core;

import com.ftx.exportexcel.rainexcel.model.ParamsModel;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author FanJiangFeng
 * @createTime 2021年08月30日 12:27:00
 */
public class SqlHandler {
    private static SqlSessionFactory sqlSessionFactory;

    /**
     * 得到对应xml的sql语句
     *
     * @param id          定位mybatis.xml的sql位置  com.ftx.exportexcel.rainbusiness.mapper.ExportMapper.getUserList
     * @param paramsModel 查询参数集合（包括各种形式）
     * @return
     */
    public static String getSql(String id, ParamsModel paramsModel) throws IOException {
        //获取执行sql
        if (sqlSessionFactory == null) {
            sqlSessionFactory = ServletContextUtil.getBean(SqlSessionFactory.class);
        }
        Configuration con = sqlSessionFactory.getConfiguration();
        Map<String, Object> map = null;
        if (paramsModel != null) {
            //得到所有参数
            map = createMapParams(paramsModel);
        }
        //得到替换掉占位符的sql
        String sql = MyBatisSql.getMyBatisSql(id, map, sqlSessionFactory);
        return sql;
    }


    /**
     * 得到所有的参数形式的参数，组长Map参数列表
     * map 封装sql需要查询的sql的条件，用于条件查询占位符替换 要综合所有参数的种类（对象，字符串，List集合，多个对象）
     * Map<String, Object> paramMap = new HashMap();
     * paramMap.put("account", "admin");
     */
    private static Map<String, Object> createMapParams(ParamsModel paramsModel) {
        //得到对象参数
        Object objParams = paramsModel.getObjParams();
        //得到对象列表参数
        List<?> objListParams = paramsModel.getObjListParams();
        //得到map参数
        Map mapParams = paramsModel.getMapParams();
        //map列表参数
        List<Map> mapListParams = paramsModel.getMapListParams();
        //校验
        if (objParams instanceof java.lang.String) throw new RuntimeException("String类型的参数的正确存放位置为mapParams");
        if (objParams instanceof java.util.Map) throw new RuntimeException("Map类型的参数的正确存放位置为mapParams");
        //初始化Map
        Map<String, Object> params = new LinkedHashMap<>();
        //如果是对象类型，得到所有类，过滤所有不是null的属性值，存入params
        if (objParams != null) processObj(objParams, params);
        if (objListParams != null && objListParams.size() > 0) processObj(objListParams, params);
        if (mapParams != null && mapParams.size() > 0) processObj(mapParams, params);
        if (mapListParams != null && mapListParams.size() > 0) processObj(mapListParams, params);
        return params;
    }

    /**
     * 将不同类型的参数组装到一起
     *
     * @param objParams 对象参数
     * @param params    返回参数列表
     */
    private static void processObj(Object objParams, Map<String, Object> params) {
        if (objParams instanceof List) {
            List<Object> list = (List<Object>) objParams;
            for (Object object : list) {
                //递归
                processObj(object, params);
            }
        } else if (objParams instanceof Map) {
            params.putAll((Map<String, Object>) objParams);
        } else {
            Field[] declaredFields = new Field[0];
            try {
                declaredFields = objParams.getClass().getDeclaredFields();
            } catch (SecurityException e) {
                throw new RuntimeException("没有找到任何属性");
            }
            Class<?> aClass = objParams.getClass();
            //对所有属性遍历，反射得到属性值
            for (Field field : declaredFields) {
                //方法名
                String name = field.getName();
                //首字母大写
                String newName = name.substring(0, 1).toUpperCase() + name.substring(1);
                String methodName = "get" + newName;
                Method declaredMethod = null;
                Object fieldValue = null;
                //得到方法
                try {
                    declaredMethod = aClass.getDeclaredMethod(methodName);
                    //属性值
                    fieldValue = declaredMethod.invoke(objParams);
                } catch (Exception e) {
                    throw new RuntimeException("找不到" + methodName + "方法");
                }
                if (fieldValue != null) {
                    params.put(name, fieldValue);
                }
            }
        }
    }
}
