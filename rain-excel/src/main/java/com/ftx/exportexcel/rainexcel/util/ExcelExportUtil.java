package com.ftx.exportexcel.rainexcel.util;

import com.ftx.exportexcel.rainexcel.core.ServletContextUtil;
import com.ftx.exportexcel.rainexcel.core.SqlHandler;
import com.ftx.exportexcel.rainexcel.mapper.ExportMapper;
import com.ftx.exportexcel.rainexcel.model.ParamsModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class ExcelExportUtil {

    /**
     * 导出excel入口
     * TODO 不支持时间格式的参数
     *
     * @param: fileUrl  test/test.xlsx
     * @param: exportName 导出文件名字 不带后缀名
     * @param: id  ExportMapper.getUserList
     * @param: paramsModel  参数对象
     */
    public static void exportExcel(String fileUrl, String exportName, String id, ParamsModel paramsModel, HttpServletResponse response) throws Exception {

        //存放xml和xlsx的文件夹的路径
        String xmlPackageUrl = "D:\\IdeaProjects\\Rain\\Resource";
        //xlsx.xml文件全路径
        String fileAllUrl = xmlPackageUrl + File.separator + fileUrl;
        String sql = SqlHandler.getSql(id, paramsModel);
        //执行sql
        ExportMapper exportMapper = ServletContextUtil.getBean(ExportMapper.class);
        List<Map> data = exportMapper.getData(sql);
        //导出excel  查询结果集   xlsx全路径
        writeExcel(data, fileAllUrl, response, exportName);
    }

    /**
     * @param result     存储每条sql的查询结果
     * @param fileAllUrl excel的绝对路径
     * @param response
     * @param exportName 导出时的文件名
     * @throws Exception
     */
    private static void writeExcel(List<Map> result, String fileAllUrl, HttpServletResponse response, String exportName) throws Exception {
        String suffix = fileAllUrl.substring(fileAllUrl.lastIndexOf(".") + 1);
        InputStream is = new FileInputStream(new File(fileAllUrl));
        HSSFWorkbook wb = null;
        Workbook wbs = null;
        if ("xls".equals(suffix)) {
            POIFSFileSystem fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);

        } else if ("xlsx".equals(suffix)) {
            wbs = WorkbookFactory.create(is);
        }
        //读取模板内所有sheet内容
        Sheet sheet = null;
        if (wb != null) {
            sheet = wb.getSheetAt(0);
        } else {
            sheet = wbs.getSheetAt(0);
        }
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
                    //开始单元格的行索引
                    int beginCell_Row = cell.getRowIndex();
                    //开始单元格的列索引
                    int beginCell_column = cell.getColumnIndex();
                    //int endCell_column = beginCell_column + row.getLastCellNum();
                    //开始单元格的标记 执行哪个sql
                    //查询结果，准备渲染excel
                    int m = 0;
                    //如果数据为空，则导出一个空的Excel
                    if(result == null || result.size() < 1){
                        sheet.removeRow(row);
                        removeRow(sheet, beginCell_Row);
                    }else{
                        //对所有数据行进行遍历
                        for (int i = beginCell_Row; i < (beginCell_Row + result.size()); i++) {
                            //创建新的行
                            Row row1 = sheet.createRow(i + 1);
                            //给新的行 遍历每个列，进行数据渲染
                            for (int j = beginCell_column; j < (beginCell_column + result.get(0).size()); j++) {
                                Cell cell1 = sheet.getRow(beginCell_Row).getCell(j);
                                String stringCellValue1 = cell1.getStringCellValue();
                                if (stringCellValue1.contains("{")) {
                                    stringCellValue1 = stringCellValue1.replace("{", "");
                                }
                                if (stringCellValue1.contains("}")) {
                                    stringCellValue1 = stringCellValue1.replace("}", "");
                                }
                                if (stringCellValue1.contains("end:")) {
                                    stringCellValue1 = stringCellValue1.replace("end:", "");
                                }
                                if (stringCellValue1.contains("begin:")) {
                                    stringCellValue1 = stringCellValue1.replace("begin:", "");
                                }

                                //创建单元格
                                Cell cell2 = row1.createCell(j);
                                // # 号开头的为数据值转换，例如 {sex#1=男,2=女,3=未知}
                                if(stringCellValue1.indexOf("#") != -1){
                                    //截取映射规则
                                    String mappedString = stringCellValue1.substring(stringCellValue1.indexOf("#")+1,stringCellValue1.length());
                                    //字段值
                                    String fieldName = stringCellValue1.substring(0,stringCellValue1.indexOf("#"));
                                    String fieldValue = result.get(m).get(fieldName).toString();
                                    //得到字段值对应关系的映射值
                                    String value = revertValue(mappedString,fieldValue);//赋值
                                    cell2.setCellValue(value);
                                }else{
                                    //赋值
                                    cell2.setCellValue((String) result.get(m).get(stringCellValue1));
                                }
                            }
                            m++;
                            if (i == ((beginCell_Row + result.size()) - 1)) {
                                //删除占位符所在的行
                                sheet.removeRow(row);
                                removeRow(sheet, beginCell_Row);
                            }
                        }
                    }
                }
            }
        }
        //导出下载
        exportFileByDownload(wbs, null, response, exportName);
    }

    /**
     * 数据值转换 得到映射对应的值
     * @param mappedString 例如：1=男,2=女,3=未知
     * @param fieldValue 例如：2
     * @return
     */
    private static String revertValue(String mappedString, String fieldValue) {
        /**
         * 1，字符串转数组
         * 2，对数组遍历，拿到等号左边和右边的值
         * 3，匹配，返回
         */String result = null;
        try {
            //转数组
            String[] arr = mappedString.split(",");
            //返回结果
            result = null;
            //对数组遍历
            for(String str : arr) {
                String[] split = str.split("=");
                String key = split[0];
                String value = split[1];
                //如果匹配到，赋值返回
                if(key.equals(fieldValue)){
                    result = value;
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("数据值转换失败",e);
        }
        return result;
    }


    /**
     * excel表格直接浏览器下载
     *
     * @param wbs                 Workbook对象
     * @param wb                  HSSFWorkbook对象     Workbook对象和HSSFWorkbook对象二选一，导出word时后者为 NULL ，导出excel时前者为 NULL
     * @param httpServletResponse
     * @param fileName            下载文件名
     * @throws Exception
     */
    public static void exportFileByDownload(Workbook wbs, HSSFWorkbook wb, HttpServletResponse httpServletResponse, String fileName) throws Exception {
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

    /**
     * 移除模板行 把${fieldName}这一行占位符行删除
     *
     * @param sheet    sheet对象
     * @param rowIndex 行索引
     */
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
