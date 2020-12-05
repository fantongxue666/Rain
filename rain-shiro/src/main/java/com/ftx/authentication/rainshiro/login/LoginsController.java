package com.ftx.authentication.rainshiro.login;

import com.ftx.authentication.rainshiro.constant.APPEnums;
import com.ftx.authentication.rainshiro.constant.JsonObject;
import com.ftx.authentication.rainshiro.model.AuthUser;
import com.ftx.authentication.rainshiro.model.Label;
import com.ftx.authentication.rainshiro.model.Role;
import com.ftx.authentication.rainshiro.model.TreeNode;
import com.ftx.authentication.rainshiro.shiro.AuthenUrlConfig;
import com.ftx.authentication.rainshiro.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName TestController.java
 * @Description TODO
 * @createTime 2020年10月19日 15:53:00
 */
@RestController
@Api(tags = "认证接口")
public class LoginsController {
    Logger log= LoggerFactory.getLogger(LoginsController.class);

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    TokenUtil tokenUtil;
    @Autowired
    ShiroDao shiroDao;
    @Autowired
    AuthenUrlConfig authenUrlConfig;

    @GetMapping("/saveRoles")
    @ApiOperation("保存角色授权")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleid",value = "角色id",required = true),
            @ApiImplicitParam(name = "powerids",value = "菜单节点id列表",required = true)
    })
    public JsonObject saveRoles(String roleid,String[] powerids){
        int i = shiroDao.delRole(roleid);
        for(String powerid:powerids){
        Role role=new Role();
        role.setId(UuidUtil.getUuid());
        role.setRoleid(roleid);
        role.setPowerid(Integer.parseInt(powerid));
        shiroDao.insertRole(role);
    }
    return JsonObject.Success();
    }

    @GetMapping("/getMenuByRole")
    @ApiOperation("获取角色对应的权限树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleid",value = "角色id",required = true)
    })
    public JsonObject<List<Label>> getMenuByRole(String roleid){
        List<Integer> powerIdsByAccount = shiroDao.getPowerIdsByAccount(roleid);
        List<Label> labelList=new ArrayList<>();
        List<TreeNode> treeList = shiroDao.getRole(roleid);
        if(treeList!=null){
            for(TreeNode treeNode:treeList){
                if(treeNode.getParentid()==0){
                    Label label=new Label();
                    label.setId(treeNode.getId());
                    label.setLabel(treeNode.getName());
                    label.setIds(powerIdsByAccount);
                    //递归绑定子节点
                    bindChildren(label,treeNode,treeList);
                    labelList.add(label);
                }
            }

            return new JsonObject(labelList,APPEnums.OK);
        }else{
            return JsonObject.Error();
        }

    }

    private void bindChildren(Label label, TreeNode treeNode, List<TreeNode> treeList) {
        List<Label> children=new ArrayList<>();
        for(TreeNode tree:treeList){
            if(tree.getParentid()==treeNode.getId()){
                Label label1=new Label();
                label1.setId(tree.getId());
                label1.setLabel(tree.getName());
                //递归绑定子节点
                bindChildren(label1,tree,treeList);
                children.add(label1);
            }
        }
        label.setChildren(children);
    }


    @GetMapping("/getTreeList")
    @ApiOperation("获取路由权限Tree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account",value = "账号",required = true)
    })
    public JsonObject<List<TreeNode>> getTreeList(String account){
        List<TreeNode> treeList = shiroDao.getTreeListByAccount(account);
        return new JsonObject(treeList,APPEnums.OK);
    }
    @GetMapping(value = "/getPersonInfo")
    @ApiOperation(value = "获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account",value = "账号",required = true)
    })
    public JsonObject<AuthUser> getPersonInfo(String account,HttpServletRequest request){
        List<AuthUser> user = shiroDao.getUserInfo(account);
        AuthUser authUser = user.get(0);
        List<String> roles = shiroDao.getRolesByUsername(account);
        authUser.setPowerList(roles);
        String token = tokenUtil.getToken(request);
        authUser.setToken(token);
        return new JsonObject(authUser,APPEnums.OK);
    }

    @PostMapping(value = "/login")
    @ApiOperation(value = "登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account",value = "用户名",required = true),
            @ApiImplicitParam(name = "pwd",value = "密码",required = true)
    })
    public JsonObject<AuthUser> mains(@ApiIgnore @RequestBody AuthUser authUser, HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token=new UsernamePasswordToken(authUser.getAccount(),authUser.getPwd());
        try {
            subject.login(token);
            String token1 = tokenUtil.setToken(request);//设置token
            List<AuthUser> user = shiroDao.getUserInfo(authUser.getAccount());
            AuthUser user1 = user.get(0);
            user1.setToken(token1);
            user1.setIp(IPUtil.getIp(request));
            List<String> roles = shiroDao.getRolesByUsername(authUser.getAccount());
            user1.setPowerList(roles);
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
    @PostMapping("/logout")
    @ApiOperation(value = "退出")
    public JsonObject<Object> logouts(HttpServletRequest request){
        Subject subject = SecurityUtils.getSubject();
        if(subject.isAuthenticated()){
            subject.logout();

        }
        String token = tokenUtil.getToken(request);
        log.info("token:"+token);
        String tokenName = authenUrlConfig.getTokenName();
        log.info("token_name:"+tokenName);
        redisUtil.remove(tokenName+"/"+token);
        log.info("已退出shiro登录状态");

        return new JsonObject<>("已退出系统");
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    @ApiOperation(value = "注册（默认注册普通用户）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account",value = "设置用户名",required = true),
            @ApiImplicitParam(name = "pwd",value = "设置密码",required = true),
            @ApiImplicitParam(name = "username",value = "设置昵称",required = true)
    })
    public JsonObject<Object> registers(@ApiIgnore AuthUser authUser) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(authUser.getPwd().getBytes());
        byte[] digest = md5.digest();
        String md5_pwd = String.format("%032x", new BigInteger(1, digest));
        log.info("加密后的密码："+md5_pwd);
        authUser.setPwd(md5_pwd);
        authUser.setId(UuidUtil.getUuid());
        int i = shiroDao.registerUser(authUser);
        return i>0?JsonObject.Success():JsonObject.Error();

    }

    /**
     * 测试redis连接
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "测试token有效")
    public String adds(){
        String id= null;
            id = "1";
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

}
