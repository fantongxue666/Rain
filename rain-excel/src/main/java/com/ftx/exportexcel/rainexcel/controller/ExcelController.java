package com.ftx.exportexcel.rainexcel.controller;

import com.ftx.exportexcel.rainexcel.config.ExcelConfig;
import com.ftx.exportexcel.rainexcel.mapper.ExportMapper;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
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
 * 说明：该版本暂不支持其他格式参数，只支持导出字符串，只支持xlsx格式，另外也只支持excel模板导出第一个sheet页的内容，待后期升级完善
 */
@Controller
public class ExcelController {
    Logger logger = LoggerFactory.getLogger(ExcelController.class);
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
    public void exportExcel(String fileUrl, String exportName, HttpServletRequest request, HttpServletResponse response) throws Exception {

        //存放xml和xlsx的文件夹的路径
        String xmlPackageUrl = excelConfig.getResource();
        //xlsx文件名 连同后缀名
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        //后缀名
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //xlsx.xml文件全路径
        String fileAllUrl = xmlPackageUrl + File.separator + fileUrl;
        File file = new File(fileAllUrl + ".xml");
        Map map = ResoveXmlUtil.resoveExcelXml(file);

        //得到访问地址的所有携带参数名称  &后跟的参数key 用于查询条件${}
        Map<String, String> parameters = new HashMap<>();
        //存储每条sql的查询结果
        Map sqlResultMap = new HashMap();
        //遍历xml中每一个sql标签中的sql语句
        for (Object key : map.keySet()) {
            String key_name = key.toString();
            String value = map.get(key_name).toString();
            //原始sql 带${}占位符
            String old_sql = value;
            String key1 = "$";
            String key2 = "}";
            int i = value.indexOf(key1);
            int j = value.indexOf(key2);
            while (i != -1 && j != -1) {

                String paramter_name = value.substring(i + 2, j);
                String parameter = request.getParameter(paramter_name);
                if (parameter == null) {
                    logger.error("请求地址中excel的参数【" + paramter_name + "】未找到对应的XML文件SQL占位符，解析参数发生错误");
                    return;
                }
                String $zwf = value.substring(i, j + 1);
                value = value.replace($zwf, "'" + parameter + "'");

                parameters.put(paramter_name, parameter);
                i = value.indexOf(key1, i + 1);
                j = value.indexOf(key2, j + 1);

            }

            //封装查询sql
            List<Map> data = exportMapper.getData(value);
            sqlResultMap.put(key, data);

        }

        //导出excel  查询结果集   xlsx全路径
        writeExcel(sqlResultMap, fileAllUrl, response, exportName);


    }

    private void writeExcel(Map sqlResultMap, String fileAllUrl, HttpServletResponse response, String exportName) throws Exception {
        String suffix = fileAllUrl.substring(fileAllUrl.lastIndexOf(".") + 1);
        InputStream is = new FileInputStream(new File(fileAllUrl));
        HSSFWorkbook wb = null;
        Workbook wbs = null;
        if ("xls".equals(suffix)) {
            POIFSFileSystem fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);
            //读取模板内所有sheet内容
            HSSFSheet sheet = wb.getSheetAt(0);
            //遍历excel中的每一行
            for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                HSSFRow row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }

            }
            this.exportExcelByDownload(null, wb, response, exportName);


        } else if ("xlsx".equals(suffix)) {
            wbs = WorkbookFactory.create(is);
            //读取模板内所有sheet内容
            Sheet sheet = wbs.getSheetAt(0);
            //遍历excel中的每一行
            for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
                Row row = sheet.getRow(rowNum);
                if (row == null) {
                    continue;
                }
                //遍历行中的每一列
                for (int cellNum = 0; cellNum <= row.getLastCellNum(); cellNum++) {
                    Cell cell = row.getCell(cellNum);
                    if (cell == null) {
                        continue;
                    }
                    //每一个单元格的值
                    String stringCellValue = cell.getStringCellValue();
                    //找到开始的单元格，得到开始的单元格的坐标
                    if (stringCellValue.contains("begin")) {
                        int beginCell_Row = cell.getRowIndex();
                        int beginCell_column = cell.getColumnIndex();
                        int endCell_column = beginCell_column + row.getLastCellNum();
                        String sql_key = stringCellValue.substring(1, stringCellValue.indexOf("_"));
                        List<Map> result = (List<Map>) sqlResultMap.get(sql_key);
                        int m = 0;
                        for (int i = beginCell_Row; i < (beginCell_Row + result.size()); i++) {
                            Row row1 = sheet.createRow(i + 1);
                            for (int j = beginCell_column; j < (beginCell_column + result.get(0).size()); j++) {
                                Cell cell1 = sheet.getRow(beginCell_Row).getCell(j);
                                String stringCellValue1 = cell1.getStringCellValue();
                                if (stringCellValue1.contains("{")) {
                                    stringCellValue1 = stringCellValue1.replace("{", "");
                                }
                                if (stringCellValue1.contains("}")) {
                                    stringCellValue1 = stringCellValue1.replace("}", "");
                                }
                                if (stringCellValue1.contains("{" + sql_key + "_end:")) {
                                    stringCellValue1 = stringCellValue1.replace("{" + sql_key + "_end:", "");
                                }
                                if (stringCellValue1.contains("{" + sql_key + "_begin:")) {
                                    stringCellValue1 = stringCellValue1.replace("{" + sql_key + "_begin:", "");
                                }
                                if (stringCellValue1.contains(sql_key + "_end:")) {
                                    stringCellValue1 = stringCellValue1.replace(sql_key + "_end:", "");
                                }
                                if (stringCellValue1.contains(sql_key + "_begin:")) {
                                    stringCellValue1 = stringCellValue1.replace(sql_key + "_begin:", "");
                                }

                                Cell cell2 = row1.createCell(j);
                                cell2.setCellValue((String) result.get(m).get(stringCellValue1));
                            }
                            m++;
                            if (i == ((beginCell_Row + result.size()) - 1)) {
                                //删除占位符所在的行
                                sheet.removeRow(row);
                               removeRow(sheet,beginCell_Row);
                            }
                        }
                    }


                }
            }

            this.exportExcelByDownload(wbs, null, response, exportName);

        }
    }


    //excel表格直接浏览器下载
    public void exportExcelByDownload(Workbook wbs, HSSFWorkbook wb, HttpServletResponse httpServletResponse, String fileName) throws Exception {
        //响应类型为application/octet- stream情况下使用了这个头信息的话，那就意味着不想直接显示内容
        //httpServletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        //attachment为以附件方式下载
        String suffix = "";
        if (wbs != null) {
            suffix = ".xlsx";
        } else if (wb != null) {
            suffix = ".xls";
        }
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(
                fileName + suffix,
                "utf-8"));
        /**
         * 代码里面使用Content-Disposition来确保浏览器弹出下载对话框的时候。
         * response.addHeader("Content-Disposition","attachment");一定要确保没有做过关于禁止浏览器缓存的操作
         */
        httpServletResponse.setHeader("Cache-Control", "No-cache");
        httpServletResponse.flushBuffer();
        if (wbs != null) {
            wbs.write(httpServletResponse.getOutputStream());
            wbs.close();
        } else if (wb != null) {
            wb.write(httpServletResponse.getOutputStream());
            wb.close();
        }

    }


    public static void removeRow(Sheet sheet, int rowIndex) {
        int lastRowNum = sheet.getLastRowNum();
        if (rowIndex >= 0 && rowIndex < lastRowNum)
            sheet.shiftRows(rowIndex + 1, lastRowNum, -1, true, false); // 将行号为rowIndex+1一直到行号为lastRowNum的单元格全部上移一行，以便删除rowIndex行
        if (rowIndex == lastRowNum) {
            Row removingRow = sheet.getRow(rowIndex);
            if (removingRow != null)
                sheet.removeRow(removingRow);
        }
    }
}
