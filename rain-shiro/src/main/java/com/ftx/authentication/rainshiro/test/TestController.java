package com.ftx.authentication.rainshiro.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName TestController.java
 * @Description TODO
 * @createTime 2020年10月19日 15:53:00
 */
@Controller
public class TestController {
    Logger log= LoggerFactory.getLogger(TestController.class);

    @RequestMapping("/test")
    @ResponseBody
    public  void main() {
        String username="18838030468";
        String password="123456";
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token=new UsernamePasswordToken(username,password);
        try {
            subject.login(token);
        }catch (UnknownAccountException e){
        }catch (IncorrectCredentialsException e){
        }
    }

    @RequestMapping("/add")
    @ResponseBody
    public void add(){
        log.info("访问了添加接口");
    }

    @RequestMapping("/noAuthen")
    @ResponseBody
    public void noAuthen(){
        log.info("没有权限");
    }

}
