package com.ftx.exportexcel.rainexcel.util;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ftx.exportexcel.rainexcel.util.ExcelExportUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTInd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

/**
 * @createTime 2021年09月08日 13:50:00
 */
public class WordExportUtil {

    //模板存放位置
    private static String rootPath = "D:\\IdeaProjects\\Rain\\Resource";
    //文档
    private static XWPFDocument doc = null;
    //文件输入流
    private static FileInputStream is = null;


    /**
     * 初始化文件输入流和文档doc对象
     * @param templatePath 模板存放位置
     *
     * @throws IOException
     */
    public static void init(String templatePath) throws IOException {
        try {
            is = new FileInputStream(new File(rootPath + File.separator + templatePath));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("没有找到模板文件",e);
        }
        doc = new XWPFDocument(is);
    }

    /**
     * 替换掉占位符
     *
     * @param params
     * @return
     * @throws Exception
     */
    public static boolean export(Map<String, Object> params) throws Exception {
        replaceInPara(doc, params);
        return true;
    }

    /**
     * 替换掉表格中的占位符
     *
     * @param params
     * @param tableIndex
     * @return
     * @throws Exception
     */
    public static boolean export(Map<String, Object> params, int tableIndex) throws Exception {
        replaceInTable(doc, params, tableIndex);
        return true;
    }

    /**
     * 循环生成表格【完整表格】
     *
     * @param params 参数列表
     * @param tableIndex 表格索引值
     * @return
     * @throws Exception
     */
    public static boolean export(List<Map<String, String>> params, int tableIndex) throws Exception {
        return export(params, tableIndex, false,null);
    }

    /**
     * 循环生成表格【表格中的某一个单元格拆分了多行多列 而成的内置表格】
     *
     * @param params 参数列表
     * @param tableIndex 表格索引值
     * @param isBuiltInCell 是否是表格单元格中的内置多行表格（一个单元格拆分成了多行多列）
     * @param templateRow isBuiltInCell为 true 时有效，内置单元格中的表格的模板行 位于整个表格的行索引
     * @return
     * @throws Exception
     */
    public static boolean export(List<Map<String, String>> params, int tableIndex, Boolean isBuiltInCell,Integer templateRow) throws Exception {
        insertValueToTable(doc, params, tableIndex, isBuiltInCell,templateRow);
        return true;
    }

    /**
     * 导出图片
     *
     * @param params
     * @return
     * @throws Exception
     */
    public boolean exportImg(Map<String, Object> params) throws Exception {
		/*List<XWPFParagraph> list = doc.getParagraphs();
		for(XWPFParagraph para : list){
			logger.info(para.getText());
		}*/
        List<XWPFTable> list = doc.getTables();
        System.out.print(list.size());
        return true;
    }

    /**
     * 生成word文档
     *
     * @return
     * @throws IOException
     */
    public static void outputToDownload(HttpServletResponse response,String fileName,String suffix) throws IOException {
        OutputStream os = response.getOutputStream();
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(
                fileName +"."+ suffix,
                "utf-8"));
        response.setHeader("Cache-Control", "No-cache");
        response.flushBuffer();
        doc.write(os);
        close(os);
        close(is);
    }

    /**
     * 替换表格里面的变量
     *
     * @param doc    要替换的文档
     * @param params 参数
     * @throws Exception
     */
    private static void replaceInTable(XWPFDocument doc, Map<String, Object> params, int tableIndex) throws Exception {
        List<XWPFTable> tableList = doc.getTables();
        if (tableList.size() <= tableIndex) {
            throw new Exception("tableIndex对应的表格不存在");
        }
        XWPFTable table = tableList.get(tableIndex);
        List<XWPFTableRow> rows;
        List<XWPFTableCell> cells;
        List<XWPFParagraph> paras;
        rows = table.getRows();
        for (XWPFTableRow row : rows) {
            cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                paras = cell.getParagraphs();
                for (XWPFParagraph para : paras) {
                    replaceInPara(para, params);
                }
            }
        }
    }

    /**
     * 替换段落里面的变量
     *
     * @param doc    要替换的文档
     * @param params 参数
     * @throws Exception
     */
    private static void replaceInPara(XWPFDocument doc, Map<String, Object> params) throws Exception {
        //得到doc中的所有段落
        Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
        XWPFParagraph para;
        //对所有段落遍历，处理每一个段落中的变量
        while (iterator.hasNext()) {
            para = iterator.next();
            //处理段落中的变量
            replaceInPara(para, params);
        }
    }

    /**
     * 替换段落里面的变量
     *
     * @param para   要替换的段落
     * @param params 参数
     * @throws Exception
     * @throws IOException
     * @throws InvalidFormatException
     */
    private static boolean replaceInPara(XWPFParagraph para, Map<String, Object> params) throws Exception {
        boolean data = false;
        List<XWPFRun> runs;
        //有符合条件的占位符
        if (matcher(para.getParagraphText()).find()) {
            runs = para.getRuns();
            data = true;
            Map<Integer, String> tempMap = new HashMap<Integer, String>();
            for (int i = 0; i < runs.size(); i++) {
                XWPFRun run = runs.get(i);
                String runText = run.toString();
                //以"$"开头
                boolean begin = runText.indexOf("$") > -1;
                boolean end = runText.indexOf("}") > -1;
                if (begin && end) {
                    tempMap.put(i, runText);
                    fillBlock(para, params, tempMap, i);
                    continue;
                } else if (begin && !end) {
                    tempMap.put(i, runText);
                    continue;
                } else if (!begin && end) {
                    tempMap.put(i, runText);
                    fillBlock(para, params, tempMap, i);
                    continue;
                } else {
                    if (tempMap.size() > 0) {
                        tempMap.put(i, runText);
                        continue;
                    }
                    continue;
                }
            }
        } else if (matcherRow(para.getParagraphText())) {
            runs = para.getRuns();
            data = true;
        }
        return data;
    }

    /**
     * 填充run内容
     *
     * @param para
     * @param params
     * @param tempMap
     * @throws InvalidFormatException
     * @throws IOException
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    private static void fillBlock(XWPFParagraph para, Map<String, Object> params,
                           Map<Integer, String> tempMap, int index)
            throws InvalidFormatException, IOException, Exception {
        Matcher matcher;
        if (tempMap != null && tempMap.size() > 0) {
            String wholeText = "";
            List<Integer> tempIndexList = new ArrayList<Integer>();
            for (Map.Entry<Integer, String> entry : tempMap.entrySet()) {
                tempIndexList.add(entry.getKey());
                wholeText += entry.getValue();
            }
            if (wholeText.equals("")) {
                return;
            }
            matcher = matcher(wholeText);
            if (matcher.find()) {
                boolean isPic = false;
                int width = 0;
                int height = 0;
                int picType = 0;
                String path = null;
                String keyText = matcher.group().substring(2, matcher.group().length() - 1);
                Object value = params.get(keyText);
                String newRunText = "";
                if (value instanceof String) {
                    newRunText = matcher.replaceFirst(String.valueOf(value));
                } else if (value instanceof Map) {//插入图片
                    isPic = true;
                    Map pic = (Map) value;
                    width = Integer.parseInt(pic.get("width").toString());
                    height = Integer.parseInt(pic.get("height").toString());
                    picType = getPictureType(pic.get("type").toString());
                    path = pic.get("path").toString();
                }

                //模板样式
                XWPFRun tempRun = null;
                // 直接调用XWPFRun的setText()方法设置文本时，在底层会重新创建一个XWPFRun，把文本附加在当前文本后面，
                // 所以我们不能直接设值，需要先删除当前run,然后再自己手动插入一个新的run。
                for (Integer pos : tempIndexList) {
                    tempRun = para.getRuns().get(pos);
                    tempRun.setText("", 0);
                }
                if (isPic) {
                    //addPicture方法的最后两个参数必须用Units.toEMU转化一下
                    //para.insertNewRun(index).addPicture(getPicStream(path), picType, "测试",Units.toEMU(width), Units.toEMU(height));
                    tempRun.addPicture(getPicStream(path), picType, "测试", Units.toEMU(width), Units.toEMU(height));
                } else {
                    //样式继承
                    if (newRunText.indexOf("\n") > -1) {
                        String[] textArr = newRunText.split("\n");
                        if (textArr.length > 0) {
                            //设置字体信息
                            String fontFamily = tempRun.getFontFamily();
                            int fontSize = tempRun.getFontSize();
                            //logger.info("------------------"+fontSize);
                            for (int i = 0; i < textArr.length; i++) {
                                if (i == 0) {
                                    tempRun.setText(textArr[0], 0);
                                } else {
                                    if (textArr[i] != null && !"".equals(textArr[i])) {
                                        XWPFRun newRun = para.createRun();
                                        //设置新的run的字体信息
                                        newRun.setFontFamily(fontFamily);
                                        if (fontSize == -1) {
                                            newRun.setFontSize(10);
                                        } else {
                                            newRun.setFontSize(fontSize);
                                        }
                                        newRun.addBreak();
                                        newRun.setText(textArr[i], 0);
                                    }
                                }
                            }
                        }
                    } else {
                        tempRun.setText(newRunText, 0);
                    }
                }
            }
            tempMap.clear();
        }
    }


    /**
     * 给表格渲染列表数据
     *
     * @param doc
     * @param params 参数列表
     * @param tableIndex 表格索引值 （第几个表格）
     * @param isBuiltInCell 是否是表格单元格中的内置多行表格（一个单元格拆分成了多行多列）
     * @param templateRow isBuiltInCell为 true 时有效，内置单元格中的表格的模板行 位于整个表格的行索引
     * @throws Exception
     */
    private static void insertValueToTable(XWPFDocument doc, List<Map<String, String>> params, int tableIndex, Boolean isBuiltInCell,
    Integer templateRow) throws Exception {
        //获取所有表格
        List<XWPFTable> tableList = doc.getTables();
        //校验表格的索引数
        if (tableList.size() <= tableIndex) {
            throw new Exception("tableIndex对应的表格不存在");
        }
        //得到指定的表格
        XWPFTable table = tableList.get(tableIndex);
        //得到表格的所有行
        List<XWPFTableRow> rows = table.getRows();
        if (rows.size() < 2) {
            throw new Exception("tableIndex对应表格应该为2行");
        }
        //拿到模板行
        XWPFTableRow tmpRow = rows.get(1);
        //初始化临时的单元格
        List<XWPFTableCell> tmpCells = null;
        List<XWPFTableCell> cells = null;
        XWPFTableCell tmpCell = null;

        //拿到模板行的所有模板单元格
        tmpCells = tmpRow.getTableCells();
        String cellText = null;
        String cellTextKey = null;

        //对所有的参数（List<Map<String, String>>）进行遍历
        for (int i = 0, len = params.size(); i < len; i++) {
            //得到当前行的参数 Map
            XWPFTableRow row = null;
            Map<String, String> map = params.get(i);
            //判断表格类型
            if(isBuiltInCell){
                //内置表格
                //在第 templateRow + 1 行的位置插入一行
                row = table.insertNewTableRow(templateRow + 1);
                //拿到第 templateRow 行
                XWPFTableRow row5 = table.getRow(templateRow);
                //复制行属性（复制属性的目的是为了插入的行要和上面的行的宽高等样式相同）
                row.getCtRow().setTrPr(row5.getCtRow().getTrPr());
                //复制列属性
                //得到第 templateRow 行所有的列
                List<XWPFTableCell> tableCells = row5.getTableCells();
                XWPFTableCell targetCell = null;
                //对第 templateRow 行所有的列遍历
                for (XWPFTableCell cell:tableCells){
                    //给插入的新行增加列
                    targetCell=row.addNewTableCell();
                    //把第 templateRow 行对应的列的属性复制给该新加列
                    targetCell.getCTTc().setTcPr(cell.getCTTc().getTcPr());
                }
            }else{
                //正常表格
                //表格创建一个新行
                row = table.createRow();
            }

            //新行设置高度（设置和模板行的高度一样）
            row.setHeight(tmpRow.getHeight());
            //得到新行的所有单元格
            cells = row.getTableCells();
            // 插入的行会填充与表格第一行相同的列数
            // 对新行的每一个单元格进行遍历
            for (int k = 0, klen = cells.size(); k < klen; k++) {
                //拿到单元格对应的模板单元格
                tmpCell = tmpCells.get(k);
                //拿到单元格
                XWPFTableCell cell = cells.get(k);
                //拿到模板单元格的字符串，也就是占位符
                cellText = tmpCell.getText();
                if (cellText != null && !"".equals(cellText)) {
                    //转换为mapkey对应的字段
                    //占位符的字段名
                    cellTextKey = cellText.replace("$", "").replace("{", "").replace("}", "");
                    if (map.containsKey(cellTextKey)) {
                        //设置
                        cell.setText(map.get(cellTextKey).toString());
                    }
                }
            }
        }
        // 删除模版行
        table.removeRow(1);
    }


    /**
     * 正则匹配字符串
     *
     * @param str
     * @return
     */
    private static Matcher matcher(String str) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher;
    }

    /**
     * 正则匹配字符串
     *
     * @param str
     * @return
     */
    private static boolean matcherRow(String str) {
        Pattern pattern = Pattern.compile("\\$\\[(.+?)\\]",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    /**
     * 根据图片类型，取得对应的图片类型代码
     *
     * @param picType
     * @return int
     */
    private static int getPictureType(String picType) {
        int res = XWPFDocument.PICTURE_TYPE_PICT;
        if (picType != null) {
            if (picType.equalsIgnoreCase("png")) {
                res = XWPFDocument.PICTURE_TYPE_PNG;
            } else if (picType.equalsIgnoreCase("dib")) {
                res = XWPFDocument.PICTURE_TYPE_DIB;
            } else if (picType.equalsIgnoreCase("emf")) {
                res = XWPFDocument.PICTURE_TYPE_EMF;
            } else if (picType.equalsIgnoreCase("jpg") || picType.equalsIgnoreCase("jpeg")) {
                res = XWPFDocument.PICTURE_TYPE_JPEG;
            } else if (picType.equalsIgnoreCase("wmf")) {
                res = XWPFDocument.PICTURE_TYPE_WMF;
            }
        }
        return res;
    }

    private static InputStream getPicStream(String picPath) throws Exception {
        URL url = new URL(picPath);
        //打开链接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置请求方式为"GET"
        conn.setRequestMethod("GET");
        //超时响应时间为5秒
        conn.setConnectTimeout(5 * 1000);
        //通过输入流获取图片数据
        InputStream is = conn.getInputStream();
        return is;
    }

    /**
     * 关闭输入流
     *
     * @param is
     */
    private static void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭输出流
     *
     * @param os
     */
    private static void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
