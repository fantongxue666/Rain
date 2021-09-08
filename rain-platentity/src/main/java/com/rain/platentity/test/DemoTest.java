package com.rain.platentity.test;

import com.rain.platentity.Util.SM3Util;
import com.rain.platentity.Util.SM4Util;
import org.apache.tomcat.util.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class DemoTest
{
    public static final String demo = "SM4加解密测试DEMO";
	public static String sortparam = "参数根据key的 ASCII 码从小到大排序（字典序）";//排序使用treemap，key1=value1&key2=value2
	public static final String appsecret = "ba22726d-14aa-11ea-9b2d-b888e3ebf769";

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

    public static void main(String[] args) throws Exception {

        // SM4加密原文
        byte[] bKey = SM4Util.generateKey();
        byte[] sm4 = SM4Util.encrypt_Ecb_Padding(bKey,demo.getBytes("UTF-8"));
        String encData = Base64.encodeBase64String(sm4);
        System.out.println("密文：" + encData);
        byte[] dd = SM4Util.decrypt_Ecb_Padding(bKey, Base64.decodeBase64(encData));
        String datainfo = new String(dd, "UTF-8");
        System.out.println("解密后的原文：" + datainfo);

		//sign签名
		sortparam+="&appsecret="+appsecret;
		byte[] signHash= SM3Util.hash(sortparam.getBytes("UTF-8"));
		StringBuilder signature = new StringBuilder();
		for (byte b : signHash) {
			signature.append(byteToHexString(b));
		}
		String sign=signature.toString();
		System.out.println("签名String值为：" + sign);
    }

	public static String byteToHexString(byte ib) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		char[] ob = new char[2];
		ob[0] = Digit[(ib >>> 4) & 0X0f];
		ob[1] = Digit[ib & 0X0F];
		String str = new String(ob);
		return str;
	}

}
