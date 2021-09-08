package com.ftx.fileserver.controller;
import com.ftx.fileserver.constant.RainLog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName FileController.java
 * @Description TODO
 * @createTime 2020年12月07日 15:52:00
 */
@RestController
public class TestController {

    @RequestMapping("/test")
    @RainLog(remark = "测试")
    public String test(String str){
        return "test";
    }
    @RequestMapping("/test2")
    public String test2(){
        return "test2";
    }


}
