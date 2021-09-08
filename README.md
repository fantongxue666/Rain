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

> 新：加入了word导出，快速开发导出word的接口

该模块作为一个starter自动配置存在

***导出Excel的使用方式***

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

**导出word的使用方式**

可支持表格，段落，表格内置表格的渲染，准备模板，如下图

![](https://img-blog.csdnimg.cn/78903a3337504550be79ee3b2a0ed9b0.png)

依赖同上

示例代码如下

```java
 @RequestMapping("/test2")
    public void test2(HttpServletResponse response){
        //测试数据准备
        String userName = "袁梦阳";
        String age = "24";
        String study = "专科学历";
        String address = "河南省新乡市原阳县某某村";
        String phone = "18833445697";

        //存放段落数据
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userName", userName);
        map.put("age", age);
        map.put("study", study);
        map.put("address", address);
        map.put("phone", phone);

        //3.表格数据
        List<Map<String,String>> excelMapList = new ArrayList<Map<String,String>>();
        Map<String,String> excelMapTemp = null;
        for (int i=1;i<11;i++) {
            excelMapTemp = new HashMap<String,String>();
            excelMapTemp.put("edu.first", "one-"+i);
            excelMapTemp.put("edu.second", "two-"+i);
            excelMapTemp.put("edu.third", "three-"+i);
            excelMapTemp.put("edu.fourth", "four-"+i);
            excelMapList.add(excelMapTemp);
        }

        List<Map<String,String>> excelMapList1 = new ArrayList<Map<String,String>>();
        Map<String,String> excelMapTemp1 = null;
        for (int i=1;i<3;i++) {
            excelMapTemp1 = new HashMap<String,String>();
            excelMapTemp1.put("first", "one-"+i);
            excelMapTemp1.put("second", "two-"+i);
            excelMapTemp1.put("third", "three-"+i);
            excelMapList1.add(excelMapTemp1);
        }


        try {
            WordExportUtil.init("test/test.docx");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            WordExportUtil.export(map);
            //0为表格的下标，第一个表格下标为0，以此类推
            WordExportUtil.export(excelMapList, 0);
            WordExportUtil.export(excelMapList1, 1,true,1);
            WordExportUtil.outputToDownload(response,"测试导出word","docx");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
```

导出结果如下图

![](https://img-blog.csdnimg.cn/48fd30d5fe7d4bb2b28fa02ebfd6607c.png)

**方法说明**

> 初始化，设置word模板位置

```
WordExportUtil.init("test/test.docx");
```

> 渲染段落（非表格）中的变量

参数为Map，例如：String phone = "18833445697";

```
WordExportUtil.export(map);
```

> 渲染普通表格

参数1：data数据  

参数2：表格索引（第几个表格）此方法适用中规中矩的表格，如上图的第一个表格

参数为 List<Map<String,String>>

```
WordExportUtil.export(excelMapList, 0);
```

> 渲染特殊表格

参数1：data数据  

参数2：表格索引（第几个表格） 

参数3：是否是特殊表格   

参数4：参数3为true时生效，内置单元格中的表格的模板行 位于整个表格的行索引

```
WordExportUtil.export(excelMapList1, 1,true,1);
```

> 导出word模板并在浏览器下载

参数1：response

参数2：导出的文件名

参数3：导出的文件后缀

```
WordExportUtil.outputToDownload(response,"测试导出word","docx");
```

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

有一些场景，比如新增一个用户，实体类User类

```java
public class Test {
    private Integer id;
    private String name;
    private String account;
    private String password;
}
```

其中密码字段是要加密之后存储到数据库的，我们平时的操作是什么？从前台传来的password字段取出并手动进行加密，然后存入User表，取出的时候还需要进行解密，然后返回明文密码的列表。

platEntity简化了这一切加解密的过程，使用platEntity之后再也不用手动加解密，只需要一个注解就可以实现复杂的逻辑，而且使用灵活方便，可以控制加密方式，是否加密，返回的列表中的字段值是否要解密。

**如何使用？**

使用方式也很简单，该platEntity模块已经上传到我的私服，如果兄弟们感兴趣的话可以尝试着用一下，不好用不收钱哈！

**首先在pom.xml配置私服**

```xml
	<repositories>
		<repository>
			<id>maven-ossez</id>
			<name>OSSEZ Repository</name>
			<url>http://nexus.tiger2.cn/nexus/content/groups/public/</url>
		</repository>
	</repositories>

	<pluginRepositories>
		<pluginRepository>
			<id>maven-ossez</id>
			<name>OSSEZ Repository</name>
			<url>http://nexus.tiger2.cn/nexus/content/groups/public/</url>
			<snapshots>
				<enabled>true</enabled>
			</snapshots>
		</pluginRepository>
	</pluginRepositories>
```

然后引入模块坐标

```xml
		<dependency>
			<groupId>com.rain.platentity</groupId>
			<artifactId>rain-platentity</artifactId>
			<version>1.2</version>
		</dependency>
```
最后不要忘记在启动类上方要加上包扫描，如果扫描不到也是不起作用的！配置`com.rain.platentity`包

> ```
> @ComponentScan(basePackages = {"你们自己的包位置","com.rain.platentity"})
> ```
**使用方式**

使用方式很简单，哪个实体类的哪个属性需要加密处理，就在该实体类上加一个@EncryptDecryptClass注解即可，然后在那个具体要加密的属性上加一个@EncryptDecryptField注解，就OK了！

<font color="red">前提：与数据库交互使用的框架是Mybatis才可使用此模块，因为用到了Mybatis拦截器！</font>

```java
@EncryptDecryptClass
public class Test {
    private Integer id;
    private String name;
    private String account;

    @EncryptDecryptField
    private String password;
}
```



其中属性注解@EncryptDecryptField的内容如下

```
    String type() default "RSA";

    boolean ParamEncrypt() default true;

    boolean resultEncrypt() default true;
```

1. 默认加密方式是RSA，可以通过type="SM4"来动态指定
2. ParamEncrypt="true/false"来指定该字段存入到表时是否需要进行加密，默认是需要加密的
3. resultEncrypt="true/false"来指定从表里查出来的列表，该字段是否需要解密，默认是需要解密的，如果为false，那么查出来的字段值则为密文

<font color="red">如果ParamEncrypt已经为false了，就不要指定resultEncrypt为true了，因为都没有加密，所以解密肯定会报错了！</font>

如果有对源码感兴趣的朋友，直接访问下方的github地址，来一波星星吧！阿门！（该jar已经整成了starter自动配置，引入可直接使用，无需在包扫描之类的）

<a href="https://github.com/fantongxue666/rain-vue-elementui-admin">附属一：点我进入Rain平台的前端Vue-Cli脚手架项目地址</a><br>
<a href="">附属二：vue-elementui-admin框架介绍- [作者: 阳哥]</a>






















