package com.lizk.jwt.demo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 文件名
 * Created at 2020/3/10
 * Created by lizongke
 * Copyright (C) 2020 SAIC VOLKSWAGEN, All rights reserved.
 */
@Service
public class AmazonFileServiceImpl {

    @Autowired
    AmazonS3 amazonServerClient;

    public InputStream downloadDocument(String bucketName, String objectKey) {
        InputStream stream = null;
        try {
            if ("http".equalsIgnoreCase("http")) {
                //查询列表
                amazonServerClient.listObjects(new ListObjectsRequest().withBucketName(bucketName));
                GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey);
                //生成公用的url
                URL url = amazonServerClient.generatePresignedUrl(urlRequest);
                stream = url.openStream();
            } else {
                S3Object object = amazonServerClient.getObject(new GetObjectRequest(bucketName, objectKey));
                if (object != null) {
                    stream = object.getObjectContent();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return stream;
    }

    public static void main(String[] args) throws Exception{
        String url="http://172.20.20.3:81/myfolder/0001/2020-02-20/9785e571-939c-47a8-b5f3-7e57c9898a6b/%E5%AF%BC%E5%87%BA.xlsx?AWSAccessKeyId=4182B2F7E60EDA1E1030&Expires=32537840686&Signature=2fh566tCFXv2Vokio40UIMAVR%2Fg%3D";
        downLoadFromUrl(url,"导出.xlsx");
    }

    public static void  downLoadFromUrl(String urlStr,String fileName) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);
        if(inputStream!=null){
            inputStream.close();
        }
        System.out.println("info:"+url+" download success");

    }

    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

}
