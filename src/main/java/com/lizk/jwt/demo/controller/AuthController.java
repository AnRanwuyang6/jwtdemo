package com.lizk.jwt.demo.controller;

import com.lizk.jwt.demo.entity.Result;
import com.lizk.jwt.demo.entity.User;
import com.lizk.jwt.demo.service.AmazonFileServiceImpl;
import com.lizk.jwt.demo.service.UserServiceImpl;
import com.lizk.jwt.demo.util.JwtUtil;
import com.lizk.jwt.demo.util.RedisUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 文件名
 * Created at 2020/3/9
 * Created by lizongke
 * Copyright (C) 2020 SAIC VOLKSWAGEN, All rights reserved.
 */
@RestController
@RequestMapping("/user")
public class AuthController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AmazonFileServiceImpl amazonFileService;

    @Autowired
    private RedisUtil redisUtil;

    private static final long expireTime=30*1000L;

    @PostMapping("/login")
    public Result login(String userName,String passWord){
        Result result=new Result();
        try {
            User user=userService.findUser(userName,passWord);
            if(StringUtils.isNotEmpty(user.getUserName())){
                //签发token
                String token= JwtUtil.createJWT(UUID.randomUUID().toString(),userName,expireTime);
                result.setData(token);
                result.setCode(Result.RESULT_CODE_0000);
            }else{
                result.setMessage("用户不存在");
                result.setCode(Result.RESULT_CODE_0001);
            }
        }catch (Exception e){
            result.setMessage("获取token失败");
            result.setCode(Result.RESULT_CODE_0001);
        }
        return  result;
    }

    @PostMapping("/getUser")
    public Result getUser(String token){
        Result result=new Result();
        try {
            if(StringUtils.isEmpty(token)){
                return unauthorized();
            }else{
                Claims claims=JwtUtil.parseJWT(token);
                String userName=claims.getSubject();
                if(StringUtils.isEmpty(userName)){
                    return unauthorized();
                }
                result.setData(userName);
                result.setCode(Result.RESULT_CODE_0000);
                return result;
            }
        }catch (ExpiredJwtException e1){
            result.setCode(Result.RESULT_CODE_0001);
            result.setMessage("未授权，请重新登录");
        }catch (Exception e){
            result.setCode(Result.RESULT_CODE_0001);
            result.setMessage("未授权，请重新登录");
        }
        return  result;
    }


    @GetMapping("/unauthorized")
    public Result unauthorized() {
        Result result = new Result();
        result.setCode(Result.RESULT_CODE_0001);
        result.setMessage("未授权，请重新登录");
        return result;
    }

    @GetMapping("/1")
    public Result download() {
        Result result = new Result();
        try {
            String bucket_name="myfolder";
            String key="0001/2020-01-09/e259e493-7b75-4552-bd98-b11cf99d7e57/123 (2).png";
            InputStream fis = amazonFileService.downloadDocument(bucket_name,key);
            //System.out.println(inputStreamToFile(fis).length);
            result.setCode(Result.RESULT_CODE_0000);
        }catch (Exception e){
            System.out.println(e.getMessage());
            result.setCode(Result.RESULT_CODE_0001);
        }
        return result;
    }

    @RequestMapping("/redis")
    public String set(){
        try {
            redisUtil.set("key-lizk","value-lizk",60);
            return "success";
        }catch (Exception e){
            return "error";
        }
    }

    @RequestMapping("/redis1")
    public String get(){
        try {
            String s=redisUtil.get("key-lizk").toString();
            return s;
        }catch (Exception e){
            return e.toString();
        }
    }

   /* private byte[] inputStreamToFile(InputStream inputStream) throws IOException {
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
    }*/
}
