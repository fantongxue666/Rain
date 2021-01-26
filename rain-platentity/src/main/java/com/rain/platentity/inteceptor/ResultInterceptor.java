package com.rain.platentity.inteceptor;

import com.rain.platentity.Util.RSAUtil;
import com.rain.platentity.anotation.EncryptDecryptClass;
import com.rain.platentity.anotation.EncryptDecryptField;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.security.PrivateKey;
import java.sql.Statement;
import java.util.*;

/**
 * Mybatis 返回值解密拦截器
 */
@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
})
@Component
public class ResultInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //得到查询的返回值结果
        Object result = invocation.proceed();
        if (Objects.isNull(result)) {
            return null;
        }
        if(result instanceof ArrayList){
            ArrayList arrayList=(ArrayList)result;
            //存放需要解密的属性
            Map<Field,Object> tempFieldsMap=new HashMap<>();
            if(arrayList.size()>0){
                for(Object obj:arrayList){
                    if(obj instanceof Map){
                        return result;
                    }else if(obj instanceof String){
                        return result;
                    }else{
                        Class<?> aClass = obj.getClass();
                        if(aClass.isAnnotationPresent(EncryptDecryptClass.class)){
                            Field[] fields = aClass.getDeclaredFields();
                            for(Field field:fields){
                                if(field.isAnnotationPresent(EncryptDecryptField.class)){
                                    tempFieldsMap.put(field,obj);
                                }
                            }

                            //解密
                            decryptFields(tempFieldsMap);
                        }

                    }
                }
            }
        }
        return result;
    }

    /**
     * 开始解密
     * @param tempFieldsMap
     */
    private void decryptFields(Map<Field, Object> tempFieldsMap) throws Exception {
        for(Map.Entry<Field,Object> m:tempFieldsMap.entrySet()){
            Field field = m.getKey();
            Object obj = m.getValue();
            field.setAccessible(true);
            //是否需要解密
            boolean b = field.getAnnotation(EncryptDecryptField.class).resultEncrypt();
            if(b){
                //加密方式
                String type = field.getAnnotation(EncryptDecryptField.class).type();
                if("RSA".equals(type)){//使用RSA解密
                    //得到密文
                    Object result = field.get(obj);
                    String value="";
                    if(result!=null){
                        value=result.toString();
                    }
                    //解密
                    PrivateKey privateKey = RSAUtil.string2PrivateKey(RSAUtil.privateKey);
                    String plainData = RSAUtil.decrypt(value, privateKey, "RSA");
                    //将解密后的结果重新赋值给属性
                    field.set(obj,plainData);
                }else if("SM4".equals(type)){

                }else{
                    throw new RuntimeException("@EncryptDecryptField注解的加密方式错误，目前只支持RSA加密和SM4加密！");
                }
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
