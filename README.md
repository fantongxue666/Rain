[TOC]
<h1>Rain</h1>
<p>
    <a href="https://img.shields.io/badge/version-V1.0.1-brightgreen">
        <img src="https://img.shields.io/badge/version-V1.0.1-brightgreen" alt="V1.0.1" />
    </a>
    <a href="https://img.shields.io/badge/author-ftx-orange">
        <img src="https://img.shields.io/badge/author-ftx-orange" alt="ftx">
    </a>
</p>

## Rain-Shiro模块

<table style="width:100%;"><tr><td bgcolor=#7FFFD4>
内容：Rain平台安全模块，登录和认证授权，Token校验
</td></tr></table>

版本更新说明：
				1，构建shiro模块，初始化shiro模块工程，并集成token，实用redis存储token<br>
                2，集成log4j2日志框架，shiro过滤器，token工具类<br>
                3，shiro模块增加MD5算法加密<br>
                4，rain平台集成swagger接口文档<br>
                5，已支持多用户多角色，权限动态分配

## Rain-Excel模块

<table style="width:100%;"><tr><td bgcolor=#7FFFD4>
内容：Rain平台Excel导出模块，使业务调用方快速开发一套导出Excel的接口
</td></tr></table>

该模块作为一个starter自动配置存在

***使用方式***

业务模块引入该starter依赖

```xml
	   <dependency>
            <groupId>com.ftx.exportexcel</groupId>
            <artifactId>rainexcel</artifactId>
            <version>1.1</version>
        </dependency>
```

然后就可以使用了！示例如下

```java
@RequestMapping("/test")
    public void test(HttpServletResponse response){
        ParamsModel paramsModel = new ParamsModel();
        Map<String,Object> map = new LinkedHashMap<>();
        User user = new User();
        user.setAccount("admin");
        user.setNum("5");
        map.put("user",user);
        paramsModel.setMapParams(map);
        ExcelExportUtil.exportExcel("test/test.xlsx","测试文件",
                    "com.ftx.exportexcel.rainbusiness.mapper.ExportMapper2.getUserList2",
                    paramsModel,response);
    }
```

共有三个步骤

> 第一，封装参数
>
> 第二，配置Excel导出模板
>
> 第三，调用工具类进行导出

`ExcelExportUtil.exportExcel`的参数说明

- fileUrl --- 模板文件的相对路径
- exportName --- 导出文件的名称（自定义）
- id --- sql语句的位置（mybatis.xml的namespace + id）
- paramsModel --- 参数
- response --- 响应

***参数容器的正确使用***

```java
public class ParamsModel {
    private Object objParams;
    private List<?> objListParams;
    private Map mapParams;
    List<Map> mapListParams;
}
```

参数为多个且互相独立的参数时，使用Map存储，例如：map.put("name","小明")，存放于mapParams或mapListParams；

参数为单个对象（内有多个属性时），此时对象应该为一个整体，使用Map存储，例如：map.put("user", user)，存放于mapParams或多个对象时存放于mapListParams；

<font color="red">如果把对象放在了objParams或者objListParams中，则会把对象中的参数拆开，那么像sql中的占位符 #{对象 . 属性} 就无法使用了，还会报错！</font>

参数为List\<String\或Integer>时，使用Map存储，例如：map.put("statusList", statusList)，存放于mapParams或mapListParams；

***不支持项***

> ↓↓ 数据库 ↓↓
>
> 当前仅支持 Mysql 库
>
> ↓↓ Excel模板 ↓↓
>
> 当前仅支持XLSX文件
>
> ↓↓ 参数 ↓↓
>
> 当前不支持Date类型的参数



## Rain-AutoCode模块

<table><tr><td bgcolor=#7FFFD4>
内容：集成了开发者工具-代码自动生成
</td></tr></table>

版本更新说明：
				该模块暂时无法和rain平台进行集成，可作为单应用，代码自动生成已内置到了Rain平台中

## Rain-License模块

<table><tr><td bgcolor=#7FFFD4>
内容：服务器证书解析和校验模块 [单应用]
</td></tr></table>

版本更新说明：
				已集成

## Rain-FileServer模块

<table><tr><td bgcolor=#7FFFD4>
内容：文件通用上传下载接口
</td></tr></table>

版本更新说明：
				1，文件上传接口<br>				2，文件下载接口<br>				3，图片在线预览接口（可直接拼装http请求地址放入\<img src=""/\>的src中进行图片的预览）

## Rain-PlatEntity模块

<table><tr><td bgcolor=#7FFFD4>
内容：用于实体类的属性加解密
</td></tr></table>

版本更新说明：
				已集成



<a href="https://github.com/fantongxue666/rain-vue-elementui-admin">附属一：点我进入Rain平台的前端Vue-Cli脚手架项目地址</a><br>
<a href="">附属二：vue-elementui-admin框架介绍- [作者: 阳哥]</a>






















