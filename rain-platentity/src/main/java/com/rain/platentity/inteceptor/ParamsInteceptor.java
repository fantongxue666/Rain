package com.rain.platentity.inteceptor;

import com.rain.platentity.Util.RSAUtil;
import com.rain.platentity.anotation.EncryptDecryptClass;
import com.rain.platentity.anotation.EncryptDecryptField;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.PublicKey;
import java.sql.PreparedStatement;
import java.util.*;

/**
 * mybatis拦截器，用于对象的属性特殊处理（此处用于加密）
 */
@Component
@Intercepts({
        @Signature(type = ParameterHandler.class, method = "setParameters", args = PreparedStatement.class)
})
public class ParamsInteceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //拦截 ParameterHandler 的 setParameters 方法 动态设置参数
        if (invocation.getTarget() instanceof ParameterHandler) {
            ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
            // 反射获取 参数对像
            Field parameterField = parameterHandler.getClass().getDeclaredField("parameterObject");
            parameterField.setAccessible(true);
            Object parameterObject = parameterField.get(parameterHandler);

            if(parameterObject!=null){
                Class<?> parameterObjectClass = null;
                if (parameterObject==null) {
                    return invocation.proceed();
                }

                if(parameterObject instanceof String){//参数是字符串
                    return invocation.proceed();
                }else if(parameterObject instanceof Map){//参数是Map
                    return invocation.proceed();
                }else if(parameterObject instanceof List){//参数是List
                    return invocation.proceed();
                }else{
                    //参数是对象，并得到对象的类
                    parameterObjectClass = parameterObject.getClass();
                    // 把当前类赋予临时类
                    Class tempClass = parameterObjectClass;
                    if(tempClass.isAnnotationPresent(EncryptDecryptClass.class)){
                        Field[] declaredFields = tempClass.getDeclaredFields();
                        //存储加了加密注解的属性集合
                        Map<Field,Object> tempFieldList=new HashMap<>();
                        for(Field field:declaredFields){
                            if(field.isAnnotationPresent(EncryptDecryptField.class)){
                                //该属性是否要加密
                                boolean isEncrypt = field.getAnnotation(EncryptDecryptField.class).ParamEncrypt();
                                if(isEncrypt){
                                    tempFieldList.put(field,parameterObject);
                                }
                            }
                        }
                        //对属性进行加密
                        if(tempFieldList.size()>0){
                            encryptFields(tempFieldList);
                        }


                    }


                }

            }
        }
        return invocation.proceed();
    }

    /**
     * 开始加密
     * @param tempFieldList
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void encryptFields(Map<Field,Object> tempFieldList) throws IllegalAccessException, InstantiationException {
        for(Map.Entry<Field,Object> m:tempFieldList.entrySet()){
            Field s = m.getKey();
            Object obj = m.getValue();
            s.setAccessible(true);
            //得到该field的加密方式
            String type = s.getAnnotation(EncryptDecryptField.class).type();
            if("RSA".equals(type)){//RSA加密
                String name = s.getName();//属性名
                try {
                    Object result = s.get(obj);
                    //得到属性值（明文）
                    String value = "";
                    if(result!=null){
                        value = result.toString();
                    }
                    //对铭文进行RSA加密
                    PublicKey publicKey = RSAUtil.string2PublicKey(RSAUtil.thirdPublicKey);
                    //进行加密并得到加密后的字符串
                    String encryptParam = RSAUtil.encrypt(value, publicKey, "RSA");
                    //加密后重新赋值
                    s.set(obj,encryptParam);

                } catch (Exception e) {
                    throw new RuntimeException("@EncryptDecryptField不可加密非字符串类型！");
                }
            }else if("SM4".equals(type)){//SM4国密加密

            }else{
                throw new RuntimeException("@EncryptDecryptField注解的加密方式错误，目前只支持RSA加密和SM4加密！");
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
