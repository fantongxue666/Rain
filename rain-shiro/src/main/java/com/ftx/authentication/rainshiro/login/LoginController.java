package com.ftx.authentication.rainshiro.login;

import com.ftx.authentication.rainshiro.constant.APPEnums;
import com.ftx.authentication.rainshiro.constant.JsonObject;
import com.ftx.authentication.rainshiro.model.AuthUser;
import com.ftx.authentication.rainshiro.utils.IPUtil;
import com.ftx.authentication.rainshiro.utils.RedisUtil;
import com.ftx.authentication.rainshiro.utils.TokenUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName TestController.java
 * @Description TODO
 * @createTime 2020年10月19日 15:53:00
 */
@Controller
public class LoginController {
    Logger log= LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    TokenUtil tokenUtil;
    @Autowired
    ShiroDao shiroDao;

    /**
     * 登录
     */
    @PostMapping("/login")
    @ResponseBody
    public JsonObject<AuthUser> main(@RequestBody AuthUser authUser, HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token=new UsernamePasswordToken(authUser.getAccount(),authUser.getPwd());
        try {
            subject.login(token);
            String token1 = tokenUtil.setToken(request);//设置token
            List<AuthUser> user = shiroDao.getUser(authUser.getAccount());
            AuthUser user1 = user.get(0);
            user1.setToken(token1);
            user1.setIp(IPUtil.getIp(request));
            return new JsonObject<>(user1);
        }catch (UnknownAccountException e){
            return new JsonObject(APPEnums.LOGIN_NAME_ERROR);
        }catch (IncorrectCredentialsException e){
            return new JsonObject(APPEnums.LOGIN_PASSWORD_ERROR);
        }
    }

    /**
     * 退出
     */
    @RequestMapping("/logout")
    @ResponseBody
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated()){
            subject.logout();
        }
        log.info("已退出shiro登录状态");
        return "logout-success";
    }

    /**
     * 测试redis连接
     * @return
     */
    @RequestMapping("/add")
    @ResponseBody
    public String add(){
        String id="1";
        //查询redis缓存中是否存在
        boolean exists = redisUtil.exists(id);
        if(exists){
            //获取缓存
            Object obj = redisUtil.get(id);
            System.out.println("从缓存中获取的数据："+obj.toString());
            return obj.toString();
        }else{
            System.out.println("模拟去mysql获取数据");
            String list="login data!";
            //数据插入缓存
            //数据插入缓存（set中的参数含义：key值，user对象，缓存存在时间10（long类型），时间单位）
            redisUtil.set(id,list,10L, TimeUnit.MINUTES);
            System.out.println("数据已存入redis");
            return list;
        }
    }

    @RequestMapping("/toLoginPage")
    @ResponseBody
    public void toLoginPage(){
        log.info("到登录页面");
    }
    @RequestMapping("/toNoAuthenPage")
    @ResponseBody
    public void toNoAuthenPage(){
        log.info("没有权限");
    }

}
