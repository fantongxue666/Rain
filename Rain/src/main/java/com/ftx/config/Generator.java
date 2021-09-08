package com.ftx.config;
import com.ftx.authentication.rainshiro.constant.JsonObject;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName Generator.java
 * @Description TODO 代码生成器的核心处理类
 * @createTime 2020年10月31日 11:37:00
 */
public class Generator {

    Logger logger= LoggerFactory.getLogger(Generator.class);

    private String templatePath;//模板路径
    private String outPath;//代码生成路径
    private Configuration cfg;

    public Generator(String outPath)throws Exception {
        this.outPath = outPath;
        //实例化Configuration对象
        cfg=new Configuration();
        //指定模板加载器
        //在代码中动态加载jar、资源文件的时候，首先应该是使用Thread.currentThread().getContextClassLoader()。
        // 如果你使用Test.class.getClassLoader()，可能会导致和当前线程所运行的类加载器不一致（因为Java天生的多线程）
        String templates=ClassUtils.getDefaultClassLoader().getResource("").getPath()+"代码自动生成模板\\";
        if(templates.contains("%e5%b9%b3%e5%8f%b0")){
            templates=templates.replace("%e5%b9%b3%e5%8f%b0","平台");
        }
        this.templatePath = templates;
        FileTemplateLoader fileTemplateLoader=new FileTemplateLoader(new File(templates));
        cfg.setTemplateLoader(fileTemplateLoader);
    }

    /**
     * todo 根据模板路径找到此路径下的所有模板文件 对每个模板进行文件生成
     * @param dataModel
     * @throws Exception
     */
    public JsonObject<Object> scanAndGenerator(Map<String,Object> dataModel)throws Exception{
        //根据模板路径找到此路径下的所有模板文件
        List<File> fileList = searchAllFile(new File(templatePath));
        try {
            //对每个模板进行文件生成
            for(File file:fileList){
                //参数1：数据模型  参数2：文件模板
                excuteGenerator(dataModel,file);
            }
            logger.info("--------- 代码自动生成已完成----------");
            return new JsonObject<>("代码已生成");
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonObject<>("代码生成失败，该表不存在，请确认表名是否正确，并且需要手动清理生成失败造成的垃圾文件然后重新生成");
        }

    }


    private void excuteGenerator(Map<String, Object> dataModel, File file) throws Exception {
        //得到模板文件的文件名+后缀名
        String one=file.getAbsolutePath();
        int i = one.indexOf("$");
        String templateFileName = one.substring(i); // ${tableName}Dao.ftl
        //判断该文件生成的模板种类
        int i1 = templateFileName.indexOf("}");
        int i2 = templateFileName.indexOf(".");
        String kindName = templateFileName.substring(i1+1, i2).toLowerCase(); // dao


        String outFileName1 = processString(templateFileName, dataModel);
        String outFileName=upperCase(outFileName1);
        //读取文件模板
        //上面把模板整个文件夹都加载到了模板加载器，所以这里拿模板只需要传入该文件夹下的文件名即可
        Template template = cfg.getTemplate(kindName+File.separator+templateFileName);
        template.setOutputEncoding("utf-8");//指定生成文件的字符集编码
        //创建文件
        File file1 = mkdir(this.outPath+kindName.toLowerCase()+File.separator, outFileName);
            //模板处理
            FileWriter fileWriter=new FileWriter(file1);
            template.process(dataModel,fileWriter);
            fileWriter.close();
            logger.info("--------- "+kindName+"模板已生成----------");

    }

    /**
     * todo ${tableName}Dao.java改为contentDao.java
     * @param templateString
     * @param dataModel
     * @return
     * @throws Exception
     */
    public String processString(String templateString,Map dataModel) throws Exception{
        StringWriter stringWriter=new StringWriter();
        Template template=new Template("ts",new StringReader(templateString),cfg);
        template.process(dataModel,stringWriter);
        return stringWriter.toString();
    }


    //查询整个目录下的所有文件
    public static List<File> searchAllFile(File dir) throws IOException {
        ArrayList arrayList=new ArrayList();
        searchFiles(dir,arrayList);
        return arrayList;

    }

    //递归获取某个目录下的所有文件
    public static void searchFiles(File dir,List<File> collector){
        if(dir.isDirectory()){
            File[] files = dir.listFiles();
            for(int i=0;i<files.length;i++){
                searchFiles(files[i],collector);
            }
        }else{
            collector.add(dir);
        }
    }

    //创建文件
    public static File mkdir(String dir,String file){
        if(dir==null){
            throw new  IllegalArgumentException("文件夹不许为空");
        }
        File result=new File(dir,file);
        if(result.getParentFile()!=null){
            result.getParentFile().mkdirs();
        }
        return result;
    }

    //首字母大写
    public String upperCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }



}
