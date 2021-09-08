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
