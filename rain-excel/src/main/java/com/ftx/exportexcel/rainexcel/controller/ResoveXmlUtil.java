package com.ftx.exportexcel.rainexcel.controller;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName ResoveXmlUtil.java
 * @Description TODO 解析xlsx.xml文件
 * @createTime 2020年10月24日 13:54:00
 */
public class ResoveXmlUtil {
    public static Map resoveExcelXml(File xmlFile){
        //创建SAXReader对象
        SAXReader saxReader = new SAXReader();
        //通过read方法读取一个文件，转换成Document对象
        Document document = null;
        try {
            document = saxReader.read(xmlFile);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        //获取根节点元素对象querySql
        Element rootElement = document.getRootElement();
        List<Element> elements = rootElement.elements("sql");
        Map map=new HashMap();
        for(Element e:elements){
            String id = e.attribute("id").getData().toString();
            String sql = e.getData().toString().trim();
            map.put(id,sql);
        }
        return map;
    }
}
