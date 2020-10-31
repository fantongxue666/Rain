package com.ftx.autocode.generation.config;

import com.ftx.autocode.generation.constant.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName AutoCodeController.java
 * @Description TODO
 * @createTime 2020年10月30日 18:43:00
 */
@RestController
@Api(tags = "开发者工具")
public class AutoCodeController {

    Logger logger= LoggerFactory.getLogger(AutoCodeController.class);

    public static Map<String,String> customMap=new HashMap<>();
    //静态块，预加载，将自定义的配置文件properties的内容全部加载到customMap中
    static {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>" +
                ">>>>>>>>>>>>>>>>> 加载sql类型和java类型的替换规则的properties文件 >>>>>>>" +
                ">>>>>>>>>>>>>>>>>>>>>>>>>");
        String path = ClassUtils.getDefaultClassLoader().getResource("javakind.properties").getPath();
        if(path.contains("%e5%b9%b3%e5%8f%b0")){
            path=path.replace("%e5%b9%b3%e5%8f%b0","平台");
        }
        File file=new File(path);
        Properties properties=new Properties();
        try {
            properties.load(new FileInputStream(file));
            customMap.putAll((Map)properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @ApiOperation(value = "代码自动生成接口")
    @PostMapping("/autoCreateCode")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tableName",value = "数据库表 例如：content表",required = true),
            @ApiImplicitParam(name = "description",value = "接口描述",required = true)
    })
    public JsonObject<Object> autoCreateCode(String tableName,String description, HttpServletRequest request) throws Exception {
    logger.info("--------- 开始自动生成代码----------");
        //获取连接
        Class.forName(driverClassName);
        Connection connection = DriverManager.getConnection(url, username, password);
        //获取元数据
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet columns = metaData.getColumns(null, null, tableName, null);
        List<Column> columnList=new ArrayList<>();
        int index=0;
        while (columns.next()){
            //列名称
            String db_column_name = columns.getString("COLUMN_NAME");
            index=1;
            //java实体的属性名
            String java_column_name = db_column_name;
            //数据库类型
            String db_type = columns.getString("TYPE_NAME");
            //java类型
            String java_type=customMap.get(db_type);
            Column column=new Column();
            column.setColumnName(db_column_name);
            column.setColumnName2(java_column_name);
            column.setColumnDbType(db_type);
            column.setColumnType(java_type);
            boolean isHave=false;
            for(Column column1:columnList){
                if(column1.getColumnName().equals(db_column_name)){
                   isHave=true;
                   break;
                }
            }
            if(!isHave){
                columnList.add(column);
            }
        }

        columns.close();
        //对table进行代码生成
        Map<String, Object> dataModel = getDataModel(columnList,tableName,description,request);
        Map config = getConfig(request);
        String outPath = config.get("outPath").toString();
        Generator generator=new Generator(outPath);
        return generator.scanAndGenerator(dataModel);

    }

    /**
     * todo 从上下文获取代码自动生成自定义配置
     */
    public Map getConfig(HttpServletRequest request){
        ServletContext servletContext = request.getSession().getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        Map map=new HashMap();
        if(webApplicationContext!=null){
            Environment environment = webApplicationContext.getBean(Environment.class);
            String packageName = environment.getProperty("rain.autocode.package-name");
            String outPath = environment.getProperty("rain.autocode.out-path");
            map.put("packageName",packageName);
            map.put("outPath",outPath);
            return map;
        }else{
            return null;
        }
    }

    /**
    * @Description 代码自动生成
    * @Date 2020/10/31 11:26
    * @return
    * @Author FanJiangFeng
    * @Version1.0
    * @History
    */



    private Map<String, Object> getDataModel(List<Column> columnList,String tableName,String description,HttpServletRequest request) {
        Map config = getConfig(request);
        String packageName = config.get("packageName").toString();
        Map<String,Object> map=new HashMap<>();
        //自定义配置
        map.putAll(customMap);
        //元数据
        map.put("table",columnList);
        map.put("pPackage",packageName);
        map.put("tableName",tableName);
        map.put("description",description);
        return map;

    }
}
