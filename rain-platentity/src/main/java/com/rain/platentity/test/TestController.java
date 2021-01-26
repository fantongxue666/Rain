package com.rain.platentity.test;
import com.rain.platentity.Util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName TestController.java
 * @Description TODO
 * @createTime 2021年01月26日 09:27:00
 */
@RestController
public class TestController {

    @Autowired
    TestService testService;

    @RequestMapping("/test")
    public int test() throws Exception {
        String test11="垃圾";
        System.out.println("加密前："+test11);

            //得到PublicKey对象
//            PublicKey publicKey = RSAUtil.string2PublicKey(RSAUtil.thirdPublicKey);
//            //进行加密并得到加密后的字符串
//            String encryptParam = RSAUtil.encrypt(test11, publicKey, "RSA");
//            System.out.println("加密后："+encryptParam);


            //自己私钥转为Key对象
//            PrivateKey privateKey = RSAUtil.string2PrivateKey(RSAUtil.privateKey);
//            //进行解密，返回解密后的原文字符串
//            String plainData = RSAUtil.decrypt(encryptParam, privateKey, "RSA");
//            System.out.println("解密后："+plainData);



        Test test=new Test();
        test.setName("test");
        test.setAccount("18838030468");
        test.setPassword("123456");
        return testService.insertEmp(test);


    }

    @RequestMapping("/get")
    public List<Test> get(){
        List<Test> list = testService.selectEmp();
        return list;
    }


}
