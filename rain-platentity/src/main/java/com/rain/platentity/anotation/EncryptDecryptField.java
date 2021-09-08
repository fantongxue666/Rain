package com.rain.platentity.anotation;

import java.lang.annotation.*;

/**
 * 需要加解密的属性注解
 */
@Documented
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptDecryptField {

    /**
     * 加密方式（默认RSA加密）
     * 加密方式支持以下种类
     * AES / DES / MD5 / RSA / SHA1 / SM3 /SM4
     */
    String type() default "RSA";

    /**
     * 传入的参数是否需要进行加密
     * 默认需要加密
     */
    boolean ParamEncrypt() default true;

    /**
     * 返回的参数是否需要进行解密
     * 默认需要解密
     */
    boolean resultEncrypt() default true;

}
