package com.ftx.authentication.rainshiro.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Collection;
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

    @Autowired
    AuthenUrlConfig authenUrlConfig;

    /**
     * 配置session监听
     * @return
     */
    @Bean("sessionListener")
    public ShiroSessionListener sessionListener(){
        ShiroSessionListener sessionListener = new ShiroSessionListener();
        return sessionListener;
    }

    /**
     * 配置会话ID生成器
     * @return
     */
    @Bean
    public SessionIdGenerator sessionIdGenerator() {
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * SessionDAO的作用是为Session提供CRUD并进行持久化的一个shiro组件
     * MemorySessionDAO 直接在内存中进行会话维护
     * EnterpriseCacheSessionDAO  提供了缓存功能的会话维护，默认情况下使用MapCache实现，内部使用ConcurrentHashMap保存缓存的会话。
     * @return
     */
    @Bean
    public SessionDAO sessionDAO() {
        EnterpriseCacheSessionDAO enterpriseCacheSessionDAO = new EnterpriseCacheSessionDAO();
        //使用ehCacheManager
//        enterpriseCacheSessionDAO.setCacheManager(ehCacheManager());
        //设置session缓存的名字 默认为 shiro-activeSessionCache
        enterpriseCacheSessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
        //sessionId生成器
        enterpriseCacheSessionDAO.setSessionIdGenerator(sessionIdGenerator());
        return enterpriseCacheSessionDAO;
    }

    /**
     * 配置保存sessionId的cookie
     * 注意：这里的cookie 不是上面的记住我 cookie 记住我需要一个cookie session管理 也需要自己的cookie
     * @return
     */
    @Bean("sessionIdCookie")
    public SimpleCookie sessionIdCookie(){
        //这个参数是cookie的名称
        SimpleCookie simpleCookie = new SimpleCookie("sid");
        //setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：

        //setcookie()的第七个参数
        //设为true后，只能通过http访问，javascript无法访问
        //防止xss读取cookie
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        //maxAge=-1表示浏览器关闭时失效此Cookie
        simpleCookie.setMaxAge(-1);
        return simpleCookie;
    }

    /**
     * 配置会话管理器，设定会话超时及保存
     * @return
     */
    @Bean("sessionManager")
    public SessionManager sessionManager() {

        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        Collection<SessionListener> listeners = new ArrayList<SessionListener>();
        //配置监听
        listeners.add(sessionListener());
        sessionManager.setSessionListeners(listeners);
        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setSessionDAO(sessionDAO());
//        sessionManager.setCacheManager(ehCacheManager());
        //全局会话超时时间（单位毫秒），默认30分钟  暂时设置为10秒钟 用来测试
        sessionManager.setGlobalSessionTimeout(1800000);
        //是否开启删除无效的session对象  默认为true
        sessionManager.setDeleteInvalidSessions(true);
        //是否开启定时调度器进行检测过期session 默认为true
        sessionManager.setSessionValidationSchedulerEnabled(true);
        //设置session失效的扫描时间, 清理用户直接关闭浏览器造成的孤立会话 默认为 1个小时
        //设置该属性 就不需要设置 ExecutorServiceSessionValidationScheduler 底层也是默认自动调用ExecutorServiceSessionValidationScheduler
        //暂时设置为 5秒 用来测试
        sessionManager.setSessionValidationInterval(3600000);
        //取消url 后面的 JSESSIONID
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;

    }

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
        //shiro加密
        HashedCredentialsMatcher hashedCredentialsMatcher=new HashedCredentialsMatcher();
        //设置加密算法
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        //设置加密次数
        hashedCredentialsMatcher.setHashIterations(1);
        //把shiro加密设置到Realm中
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        securityManager.setRealm(userRealm);
        //配置自定义session管理，使用ehcache 或redis
        securityManager.setSessionManager(sessionManager());
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
        String[] freeUrls = authenUrlConfig.getFreeUrls();
        for(String url:freeUrls){
            filterMap.put(url,"anon");
        }
        filterMap.put("/**", "authc");
        factoryBean.setFilterChainDefinitionMap(filterMap);

        //设置登录请求(必须认证才能访问，会自动跳转到下面的登录路径)
        factoryBean.setLoginUrl("/toLoginPage");
        //未授权页面
        factoryBean.setUnauthorizedUrl("/toNoAuthenPage");
        Map<String, Filter> filters = factoryBean.getFilters();
        //启动过滤器
        filters.put("authc",new ShiroLoginFilter());

        return factoryBean;
    }

    //配置shiro过滤器
    @Bean
    public ShiroLoginFilter getShiroLoginFilter(){
        return new ShiroLoginFilter ();
    }
    @Bean
    public FormAuthenticationFilter getFormAuthenticationFilter(){
        return new FormAuthenticationFilter ();
    }

//    @Bean
//    public FilterRegistrationBean myLoginRegistrationBean() {
//        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
//        filterRegistration.setFilter(getFormAuthenticationFilter());
//        //  该值缺省为false,表示生命周期由SpringApplicationContext管理,设置为true则表示由ServletContainer管理
//        filterRegistration.addInitParameter("targetFilterLifecycle", "true");
//        filterRegistration.setAsyncSupported(true);
//        filterRegistration.setEnabled(true);
//        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
//        return filterRegistration;
//    }
}

