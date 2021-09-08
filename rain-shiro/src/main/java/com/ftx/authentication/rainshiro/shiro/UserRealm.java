package com.ftx.authentication.rainshiro.shiro;

import com.ftx.authentication.rainshiro.model.AuthUser;
import com.ftx.authentication.rainshiro.login.ShiroDao;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName UserRealm.java
 * @Description TODO
 * @createTime 2020年10月19日 15:39:00
 */
public class UserRealm extends AuthorizingRealm {

    Logger log= LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    ShiroDao shiroDao;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("Shiro认证结束，开始授权");
        String username= (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
        //1，从数据库中或缓存中获取角色权限数据
        List<String> roles=shiroDao.getRolesByUsername(username);
        for(String role:roles){
            simpleAuthorizationInfo.addRole(role);
        }

        //再获取操作权限数据
//        Set<String> permissions=getPermissionsByUsername(username);
//        simpleAuthorizationInfo.setStringPermissions(permissions);

        return simpleAuthorizationInfo;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("Shiro开始认证");
        //3，得到传来的账号密码主体
        UsernamePasswordToken userToken=(UsernamePasswordToken)token;
        //1，从主体传过来的认证信息中，获得用户名
        String username= (String) token.getPrincipal();
        //2，通过用户名到数据库中获取密码
        List<AuthUser> user = shiroDao.getUser(username);
        String password=null;
        Md5Hash md5Hash=null;
        if(user.size()>0){
             password = user.get(0).getPwd();
             md5Hash=new Md5Hash(new String(userToken.getPassword()));
        }else{
            log.info("认证失败！该账号不存在");
            return null; //抛出异常 UnknownAccountException
        }
       if(!md5Hash.toString().equals(password)){
            log.info("认证失败！密码错误");
            return null;
        }

        //密码认证 是shiro自动管理密码的
        //第一个参数username会自动被传到上面的授权方法中
        log.info("认证成功");
        return new SimpleAuthenticationInfo(username,password,"UserRealm");
    }
}
