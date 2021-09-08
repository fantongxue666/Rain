package com.rain.platentity.Util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName MD5Util.java
 * @Description TODO
 * @createTime 2021年06月03日 10:14:00
 */
public class MD5Util {
    /**
     * MD5加密
     * @param source	源字符串
     * @return	加密后的密文
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String encode(String source) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        // 确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        // 加密后的字符串
        byte[] bytes = md5.digest(source.getBytes("utf-8"));
        return bytesToHexString(bytes);
    }

    private static String bytesToHexString(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (int i = 0,j=bArray.length; i < j; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp);
        }
        return sb.toString();
    }

    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String test = encode("测试一下");
        System.out.println(test);
        String test1 = encode("测试一下11");
        System.out.println(test1);
        String test2 = encode("测试一下");
        System.out.println(test2);
    }
}
