package com.ftx.fileserver.controller;

import com.ftx.fileserver.constant.APPEnums;
import com.ftx.fileserver.constant.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author FanJiangFeng
 * @version 1.0.0
 * @ClassName FileController.java
 * @Description TODO
 * @createTime 2020年12月07日 15:52:00
 */
@RestController
@Api(tags = "文件通用上传下载接口")
public class FileController {

    @Value("${file.upload}")
    private String path;

    @PostMapping("upload")
    @ApiOperation(value = "文件上传")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file",value = "文件",required = true),
            @ApiImplicitParam(name = "newFileName",value = "新文件名",required = true)
    })
    public JsonObject uploadFile(MultipartFile file, String newFileName) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        FileUtils.copyInputStreamToFile(file.getInputStream(),new File(path+File.separator+newFileName+suffix));
        Map map=new HashMap();
        map.put("fileLocation",path+File.separator+newFileName+suffix);
        return new JsonObject(map, APPEnums.OK);
    }

    @GetMapping("download")
    @ApiOperation(value = "文件下载")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileName",value = "文件路径+文件名（例如：D:\\IdeaProjects\\Rain平台\\File\\test.jpg）",required = true),
            @ApiImplicitParam(name = "newFileName",value = "下载的新文件名",required = true),
    })
    public void downloadFile(String fileName,String newFileName, HttpServletResponse response) throws IOException {
        String substring = fileName.substring(fileName.lastIndexOf("."));
        response.setHeader("content-disposition", "attachment;filename="+ URLEncoder.encode(newFileName+substring, "UTF-8"));
        FileUtils.copyFile(new File(fileName),response.getOutputStream());
        response.getOutputStream().close();
    }

    /**
     * http://localhost:8080/online?fileName=test.jpg
     * <img width="300px;height:450px;" src="http://localhost:8080/online?fileName=test.jpg" alt="测试图片在线预览">
     * @param fileName
     * @param response
     * @throws Exception
     */
    @GetMapping("online")
    @ApiOperation(value = "图片在线预览地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileName",value = "文件名（例如：test.jpg）",required = true)
    })
    public void showImage(String fileName, HttpServletResponse response) throws Exception{
        response.setContentType("image/jpg");
        FileInputStream fs=new FileInputStream(new File(path+File.separator+fileName));
        OutputStream os=response.getOutputStream();
        int lenth;
        byte[] bytes=new byte[1024];
        while((lenth=fs.read(bytes))>0){
            os.write(bytes,0,lenth);
        }
        fs.close();
        os.close();
    }




}
