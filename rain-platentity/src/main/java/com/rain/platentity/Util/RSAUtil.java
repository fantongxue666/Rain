package com.rain.platentity.Util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName RSAUtil.java
 * @Description TODO
 * @createTime 2021年01月26日 16:23:00
 */
public class RSAUtil {
    //加密算法
    private static final String KEY_ALGORITHM = "RSA";
    //密钥长度
    private static final int KEY_SIZE = 1024;
    //分块加密，单块明文的最大字节数 (KEY_SIZE/8) - 11
    private static final int MAX_ENCRYPT_BLOCK = 117;
    //分块解密，单块密文的最大字节数 KEY_SIZE/8
    private static final int MAX_DECRYPT_BLOCK = 128;
    //加密算法
    public static final String encryptAlgorithm="RSA/ECB/PKCS1Padding";
    //签名算法
    public static final String signAlgorithm="SHA1withRSA";

    //自己的私钥
    public static final String privateKey="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIZ9vUm6+c9fvX93/9bDGvBFDnTh7+G/2kQnQq+Kflh5leAnR2vthkKX8bGCrMHSADGpcX4cW9Kh1F5rd2/xYsCAar++0Z2/eEtSGmWldjNOiSvLyHQzYxVoHmkexH8gqakPzqvi+QHFuYGs1Yg9gFwIU5k2n7hxqo3tl8Z3Xq/zAgMBAAECfyKsSCC6E61jAXfbeBnmMwu+wftgRHfu/laTyJ15Fxm4PuuLNN6eXf7NLYKWLfPKMEKdbKUWqpWjIx00l5ZEpTCbubQbTIyPhSMfCz3BrSfS180M9uggww9joZUvgA8KrNJ/0N4u43N8rE5xbeiCNAmIrWMK5+7IpFb+IgoUDKECQQDN3WeLBDcIetYlU8WE03LaovUoKrFkyxEoK1NMbbgS7qkK/sCbt6HYuFGiWFH+AlnMyBQuMtMaxMukpSbp01nbAkEApz6PKCfy5URyr/PDz4Au/4qUJIanTAADqd/M1eVYWHW1pT76zBQ/cGbWYNjwq6pv2668bCNLZC76AUu2AZBZyQJBAJVhOhga1+A0Wh292s6WLHpP0vrxd8V+wJVYUtpbpz09prYOwy57IlrC7KGT2vMpO4YoDNS1FpAPCwIOwNzc+w8CQQCEs6XhpfkcI7igllobbklcaHftl45tRmGhy7t7SPRVlGiiCpvzjGld5atawygL/jutYzOM1mB+yKuEla+ABCrJAkAd1Klak29aoFQaIqDOUPQDK8VaDuUi/4Rfo48o0HLjjWFiIlETQ2Dx2/9qIIYSKNJbLwf9MRA20GdwK8m6f6Et";
    //对方的公钥
    public static final String thirdPublicKey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGfb1JuvnPX71/d//WwxrwRQ504e/hv9pEJ0Kvin5YeZXgJ0dr7YZCl/GxgqzB0gAxqXF+HFvSodRea3dv8WLAgGq/vtGdv3hLUhplpXYzTokry8h0M2MVaB5pHsR/IKmpD86r4vkBxbmBrNWIPYBcCFOZNp+4caqN7ZfGd16v8wIDAQAB";

    /**
     * 将Base64编码的公钥转化为公钥对象
     * create time: 2019年4月18日下午3:46:01
     * @param publicKeyStr
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PublicKey string2PublicKey(String publicKeyStr) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        //把base64格式的公钥进行base64解码转为byte类型
        byte[] keyBytes = Base64.decodeBase64(publicKeyStr);
        //根据给定的编码密钥创建一个新的 X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        //getInstance  api解释：返回转换指定算法的 public/private 关键字的 KeyFactory 对象
        //keyFactory是密钥规范
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);//参数是加密算法RSA
        //根据提供的密钥规范（密钥材料）生成公钥对象
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 将Base64编码的私钥转化为私钥对象
     * create time: 2019年4月18日下午3:46:32
     * @param privateKeystr
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey string2PrivateKey(String privateKeystr) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        //把base64格式的私钥进行base64解码转为byte类型
        byte[] keyBytes = Base64.decodeBase64(privateKeystr);
        //根据给定的编码密钥创建一个新的 PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        //getInstance  api解释：返回转换指定算法的 public/private 关键字的 KeyFactory 对象
        //keyFactory是密钥规范
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);//参数是加密算法RSA
        //根据提供的密钥规范（密钥材料）生成私钥对象
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    /**
     * RSA加密方法
     * create time: 2019年4月18日下午3:38:18
     * @param plainData 	待加密的内容
     * @param key 			加密使用的秘钥（对方的公钥）
     * @param algorithm 	加密使用的模式
     * @return	内容加密后的Base64编码形式
     * @throws Exception
     */
    public static String encrypt(String plainData, Key key, String algorithm) throws Exception {
        //字节数组输出流在内存中创建一个字节数组缓冲区，所有发送到输出流的数据保存在该字节数组缓冲区中
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] chiperDataBytes;
        try {
            //得到cipher实例
            Cipher cipher = Cipher.getInstance(algorithm);//参数：加密算法RSA
            //初始化   ENCRYPT_MODE：用于将 Cipher 初始化为加密模式的常量   key：密钥（对方公钥）
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //把要加密的字符串转换为byte数组
            byte[] plainDataBytes = plainData.getBytes("UTF-8");
            //调用分段加密方法进行加密
            //MAX_ENCRYPT_BLOCK：单块明文最大字节数
            //out 字节数组缓冲区，和stringbuffer类似用法
            handleData(out, cipher, plainDataBytes, MAX_ENCRYPT_BLOCK);
            //把字节数组输出流转为byte数组
            chiperDataBytes = out.toByteArray();
        } catch(Exception e) {
            throw new Exception(e);
        } finally {
            out.close();
        }
        //再把byte数组转为Base64编码格式的字符串并返回（加密后的Base64字符串）
        return Base64.encodeBase64String(chiperDataBytes);
    }

    /**
     * RSA分段解密方法
     * create time: 2019年4月18日下午3:40:52
     * @param cipherData	待解密的内容的Base64编码形式的数据
     * @param key			解密使用的秘钥
     * @param algorithm		解密使用的模式
     * @return	解密后的内容
     * @throws Exception
     */
    public static String decrypt(String cipherData, Key key, String algorithm) throws Exception, IOException {
        //字节数组输出流在内存中创建一个字节数组缓冲区，所有发送到输出流的数据保存在该字节数组缓冲区中
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] plainDataBytes;
        try {
            //得到cipher实例
            Cipher cipher = Cipher.getInstance(algorithm);//参数：加密算法RSA
            //初始化   DECRYPT_MODE:用于将 Cipher 初始化为解密模式的常量   key:密钥（自己私钥）
            cipher.init(Cipher.DECRYPT_MODE, key);
            //把要解密的字符串转换为byte数组
            byte[] chiperDataBytes = Base64.decodeBase64(cipherData);
            //调用分段解密方法进行解密
            handleData(out, cipher, chiperDataBytes, MAX_DECRYPT_BLOCK);
            //把byte 数组输出流转为byte数组
            plainDataBytes = out.toByteArray();
        } catch(Exception e) {
            throw new Exception(e);
        } finally {
            out.close();
        }
        //再把byte数组转为字符串并返回（解密后的字符串）
        return new String(plainDataBytes);
    }

    /**
     * 分段处理加解密数据，放到输出流中
     * create time: 2019年4月18日下午3:42:37
     * @param out		输出流
     * @param cipher	加解密工具
     * @param dataBytes	要处理的数据
     * @param maxBlock	处理的块的最大字节数
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    private static void handleData(ByteArrayOutputStream out, Cipher cipher, byte[] dataBytes, int maxBlock) throws IllegalBlockSizeException, BadPaddingException {
        //计算待加密的byte数组的长度
        int inputLen = dataBytes.length;
        int offSet = 0;
        int i = 0;
        byte[] cache;
        //循环减去已经被加密的长度（剩余长度）
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > maxBlock) {
                //加密操作
                //处理dataBytes字节数组的缓冲区从offset开始（包含）的前maxBlock个字节
                cache = cipher.doFinal(dataBytes, offSet, maxBlock);
            } else {
                cache = cipher.doFinal(dataBytes, offSet, inputLen - offSet);
            }
            //将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此 byte 数组输出流
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * maxBlock;
        }
    }

    /**
     * 签名算法
     * @param attributes	属性数据   要签名的字符串
     * @param privateKey	签名使用的密钥   自己的私钥
     * @param algorithm		签名使用的模式  SHA1withRSA
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static String sign(String attributes, PrivateKey privateKey, String algorithm)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        //Signature对象可用来生成和验证数字签名
        Signature signature = Signature.getInstance(algorithm);
        //初始化这个用于签名的对象
        signature.initSign(privateKey);
        //使用指定的 byte 数组更新要签名或验证的数据
        signature.update(attributes.getBytes());
        //sign()方法：返回所有已更新数据的签名字节
        byte[] signedData = signature.sign();
        //把签名字节转为Base64编码格式的字符串
        return Base64.encodeBase64String(signedData);
    }

    /**
     * 验签算法
     * @param attributes	属性数据明文
     * @param signData		签名
     * @param publicKey		验签使用的公钥
     * @param algorithm		验签使用的模式
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static boolean verifySign(String attributes, String signData, PublicKey publicKey, String algorithm) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        //Signature对象可用来生成和验证数字签名
        Signature signCheck = Signature.getInstance(algorithm);
        //使用来自给定证书的公钥初始化此用于验证的对象
        signCheck.initVerify(publicKey);
        //使用指定的 byte 数组更新要签名或验证的数据。
        signCheck.update(attributes.getBytes());
        //验证传入的签名
        return signCheck.verify(Base64.decodeBase64(signData));
    }
}

