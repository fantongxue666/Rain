package com.ftx.authentication.rainshiro.shiro;

import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName ShiroConfig.java
 * @Description TODO
 * @createTime 2020年10月19日 15:40:00
 */
@Configuration
public class ShiroConfig{
    /**
     *
     *         Subject 用户
     *         SecurityManager  管理所有用户
     *         Realm  连接数据
     *
     * 第一步，创建Realm对象  ----连接数据
     * 第二步，创建DefaultWebSecurityManager环境    ----关联Realm
     * 第三步，创建ShiroFilterFactoryBean     ----将第二部的环境设置进去
     */

    //创建Realm对象，自定义类
    @Bean
    public UserRealm userRealm(){
        return new UserRealm();
    }

    // @Bean(name = "securityManager")是把该对象加到spring容器
    @Bean(name = "securityManager")
    public DefaultSecurityManager getDefaultSecurityManager(@Qualifier("userRealm") UserRealm userRealm){
        DefaultSecurityManager securityManager=new DefaultWebSecurityManager();
        //关联Realm对象
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    //@Qualifier("securityManager")是根据名字从spring容器中拿到该对象
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultSecurityManager defaultSecurityManager){
        ShiroFilterFactoryBean factoryBean=new ShiroFilterFactoryBean();
        //设置安全管理器
        factoryBean.setSecurityManager(defaultSecurityManager);

        //添加shiro的内置过滤器
        /**
         * anon:无需认证就可以访问
         * authc:必须认证了才能访问
         * user:必须拥有  记住我 功能才可以访问
         * perms:拥有对某个资源的权限才能访问
         * role:拥有某个角色权限才能访问
         */
        Map<String,String> filterMap=new LinkedHashMap<>();
        //设置必须认证才能访问
        filterMap.put("/add","authc");
//        filterMap.put("/update","authc");
//        filterMap.put("/update/*","authc");/*是通配符
        factoryBean.setFilterChainDefinitionMap(filterMap);

        //设置登录请求(必须认证才能访问，会自动跳转到下面的登录路径)
        factoryBean.setLoginUrl("/login");
        //未授权页面
        factoryBean.setUnauthorizedUrl("/noAuthen");

        return factoryBean;
    }
}

