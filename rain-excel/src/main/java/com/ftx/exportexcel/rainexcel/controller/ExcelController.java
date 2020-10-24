package com.ftx.exportexcel.rainexcel.controller;

import com.ftx.exportexcel.rainexcel.config.ExcelConfig;
import com.ftx.exportexcel.rainexcel.mapper.ExportMapper;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName ExcelController.java
 * @Description TODO 导出excel接口
 * @createTime 2020年10月24日 11:40:00
 */

/**
 * 说明：该版本暂不支持其他格式参数，只支持字符串，另外也只支持excel模板导出第一个sheet页的内容，待后期升级完善
 */
@Controller
public class ExcelController {
    Logger logger= LoggerFactory.getLogger(ExcelController.class);
    @Autowired
    ExcelConfig excelConfig;
    @Autowired
    ExportMapper exportMapper;

    @RequestMapping("/exportExcel")
    @ResponseBody
    /**
    * @Description 解析xml中的sql和占位符
    * @Date 2020/10/24 14:09
     * @param: fileUrl todo    test/test.xlsx
     * @param: exportName 导出文件名字 不带后缀名
     * @param: request
    * @return void
    * @Author FanJiangFeng
    * @Version1.0
    * @History
    */
    //http://localhost:8080/exportExcel?fileUrl=test/测试Excel导出模板.xlsx&exportName=测试导出&id=4977fdafa7654baaa6fc200493ea2b5b&username=test
    public void exportExcel(String fileUrl, String exportName, HttpServletRequest request) throws IOException{

        //存放xml和xlsx的文件夹的路径
        String xmlPackageUrl=excelConfig.getResource();
        //xlsx文件名 连同后缀名
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/")+1);
        //后缀名
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //xlsx.xml文件全路径
        String fileAllUrl=xmlPackageUrl+File.separator+fileUrl;
        File file=new File(fileAllUrl+".xml");
        Map map = ResoveXmlUtil.resoveExcelXml(file);

        //得到访问地址的所有携带参数名称  &后跟的参数key 用于查询条件${}
        Map<String,String> parameters=new HashMap<>();
        //存储每条sql的查询结果
        Map sqlResultMap=new HashMap();
        //遍历xml中每一个sql标签中的sql语句
        for(Object key : map.keySet()){
            String key_name = key.toString();
            String value=map.get(key_name).toString();
            //原始sql 带${}占位符
            String old_sql=value;
            String key1 = "$";
            String key2="}";
            int i=value.indexOf(key1);
            int j= value.indexOf(key2);
            while(i!=-1&&j!=-1){

                String paramter_name = value.substring(i + 2, j);
                String parameter = request.getParameter(paramter_name);
                if(parameter==null){
                    logger.error("请求地址中excel的参数【"+paramter_name+"】未找到对应的XML文件SQL占位符，解析参数发生错误");
                    return;
                }
                String $zwf = value.substring(i, j + 1);
                 value = value.replace($zwf, "'"+parameter+"'");

                parameters.put(paramter_name,parameter);
                i=value.indexOf(key1,i+1);
                j=value.indexOf(key2,j+1);

            }

            //封装查询sql
            List<Map> data = exportMapper.getData(value);
            sqlResultMap.put(key,data);

        }

        //导出excel  查询结果集   xlsx全路径
        writeExcel(sqlResultMap,fileAllUrl);


    }

    private void writeExcel(Map sqlResultMap, String fileAllUrl) throws IOException {
        String suffix = fileAllUrl.substring(fileAllUrl.lastIndexOf(".")+1);
        InputStream is=new FileInputStream(new File(fileAllUrl));
        HSSFWorkbook wb=null;
        Workbook wbs=null;
        if("xls".equals(suffix)){
            POIFSFileSystem fs=new POIFSFileSystem(is);
             wb=new HSSFWorkbook(fs);
            //读取模板内所有sheet内容
            HSSFSheet sheet = wb.getSheetAt(0);
            //遍历excel中的每一行
            for(int rowNum=0;rowNum<=sheet.getLastRowNum();rowNum++){
                HSSFRow row = sheet.getRow(rowNum);
                if(row==null){
                    continue;
                }
                //遍历行中的每一列
                for(int cellNum = 0; cellNum<=row.getLastCellNum();cellNum++){
                    HSSFCell cell = row.getCell(cellNum);
                    if(cell==null){
                        continue;
                    }
                    System.out.println(cell.getStringCellValue());
                }
            }


        }else if("xlsx".equals(suffix)){
            wbs = WorkbookFactory.create(is);
            //读取模板内所有sheet内容
            Sheet sheet = wbs.getSheetAt(0);
            //遍历excel中的每一行
            for(int rowNum=0;rowNum<=sheet.getLastRowNum();rowNum++){
                Row row = sheet.getRow(rowNum);
                if(row==null){
                    continue;
                }
                //遍历行中的每一列
                for(int cellNum = 0; cellNum<=row.getLastCellNum();cellNum++){
                    Cell cell = row.getCell(cellNum);
                    if(cell==null){
                        continue;
                    }
                    //每一个单元格的值
                    String stringCellValue = cell.getStringCellValue();
                    //找到开始的单元格，得到开始的单元格的坐标
                    if(stringCellValue.contains("begin")){
                        int beginCell_Row = cell.getRowIndex();
                        int beginCell_column = cell.getColumnIndex();
                        int endCell_column=beginCell_column+row.getLastCellNum();
                        //从这继续写
                    }


                }
            }

        }
    }




}
