package com.lizk.jwt.demo.controller;

import com.lizk.jwt.demo.service.AmazonFileServiceImpl;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件名
 * Created at 2020/3/12
 * Created by lizongke
 * Copyright (C) 2020 SAIC VOLKSWAGEN, All rights reserved.
 */
@RestController
@RequestMapping("/task")
@CrossOrigin
public class DocumentController {
    @Autowired
    private AmazonFileServiceImpl amazonFileService;

    @GetMapping("/document")
    public List<Map<String,Object>> getAttachments(String documentIds,HttpServletResponse response){
        //返回结果
        List<Map<String, Object>> attachments = new ArrayList<>();
        if(StringUtils.isNotEmpty(documentIds)){
            String [] documentIdArray=documentIds.split(",");
            for(String documentId:documentIdArray ){
                Map<String, Object> map = new HashMap<String, Object>();
                try {
                    //根据documentId 获得mainfileId 然后获得 file,以及文件名，访问路径
                    //返回 mainfile 的文件流,文件名称,访问路径

                    String bucket_name="myfolder";
                    String obobjectKey="0001/2020-01-08/8389d42f-f22f-4355-897f-5d498f585a8d/文档中台.docx";
                    //下载文件
                    InputStream fis = amazonFileService.downloadDocument(bucket_name,obobjectKey);
                    byte[] bytes = this.inputStreamToFile(fis);
                    String code = "";
                    if (bytes != null && bytes.length > 0) {
                        code = this.byteToBaseString(bytes);
                    }
                    map.put("data", bytes);//文件流对应的base64编码
                    map.put("filename", "文档中台.docx");//文件名称
                    map.put("attachmentUrl","http://localhost:9010/v1/storage/download/file/8389d42f-f22f-4355-897f-5d498f585a8d");//文件访问路径
                    attachments.add(map);

                }catch (Exception e){
                    map.put("data", "");
                    map.put("filename", "");
                    map.put("attachmentUrl","");
                    attachments.add(map);
                }
            }
        }
        return attachments;
    }

    @GetMapping("/document1")
    public Map <String,String> getAttachments1(String documentIds){

        Map<String, String> map = new HashMap<String, String>();
        map.put("data", "d2VsY29tZSB0byBiZWppbmcNCg==");//文件流对应的base64编码
        map.put("filename", "文档中台.docx");//文件名称
        map.put("attachmentUrl","http://localhost:9010/v1/storage/download/file/8389d42f-f22f-4355-897f-5d498f585a8d");//文件访问路径
        System.out.println("成功+-------------------------");
        return map;
    }



    private byte[] inputStreamToFile(InputStream inputStream) throws IOException {
        String numStr = "8192";
        int num = Integer.parseInt(numStr);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        int bytesRead = 0;
        byte[] buffer = new byte[num];
        while ((bytesRead = inputStream.read(buffer, 0, num)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inputStream.close();
        return data;
    }
    public static String byteToBaseString(byte[] b) {
        return Base64.encodeBase64String(b);
    }
}
